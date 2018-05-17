package ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.write;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.hibernate.CacheMode;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUserRepository;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementRepository;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.write.apiuser.ApiUserDocument;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.write.apiuser.ApiUserElasticsearchRepository;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.write.jobadvertisement.JobAdvertisementDocument;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.write.jobadvertisement.JobAdvertisementElasticsearchRepository;

@Service
public class ElasticsearchIndexService {

    private static final int BUFFER_SIZE = 100;
    public static final String INDEX_NAME_JOB_ADVERTISEMENT = "job-advertisements";
    public static final String INDEX_NAME_API_USER = "api-users";
    public static final String TYPE_JOB_ADVERTISEMENT = "job-advertisement";
    public static final String TYPE_API_USER = "api-user";

    private final Logger log = LoggerFactory.getLogger(ElasticsearchIndexService.class);

    private final EntityManager entityManager;

    private JobAdvertisementRepository jobAdvertisementRepository;

    private JobAdvertisementElasticsearchRepository jobAdvertisementElasticsearchRepository;

    private ApiUserRepository apiUserRepository;

    private ApiUserElasticsearchRepository apiUserElasticsearchRepository;

    private final ElasticsearchTemplate elasticsearchTemplate;


    public ElasticsearchIndexService(
            EntityManager entityManager,
            JobAdvertisementRepository jobAdvertisementRepository,
            JobAdvertisementElasticsearchRepository jobAdvertisementElasticsearchRepository,
            ApiUserRepository apiUserRepository,
            ApiUserElasticsearchRepository apiUserElasticsearchRepository,
            ElasticsearchTemplate elasticsearchTemplate) {
        this.entityManager = entityManager;
        this.jobAdvertisementRepository = jobAdvertisementRepository;
        this.jobAdvertisementElasticsearchRepository = jobAdvertisementElasticsearchRepository;
        this.apiUserRepository = apiUserRepository;
        this.apiUserElasticsearchRepository = apiUserElasticsearchRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Async
    @Transactional(readOnly = true)
    public void reindexAll() {
        reindexForClass(JobAdvertisementDocument.class, jobAdvertisementRepository, jobAdvertisementElasticsearchRepository, JobAdvertisementDocument::new, "streamAllPublished");
        reindexForClass(ApiUserDocument.class, apiUserRepository, apiUserElasticsearchRepository, ApiUserDocument::new, "streamAll");

        log.info("Elasticsearch: Successfully performed reindexing");
    }

    @SuppressWarnings("unchecked")
    <JPA, ELASTIC, ID extends Serializable, DOCUMENTID extends Serializable> void reindexForClass(
            Class<ELASTIC> documentClass,
            JpaRepository<JPA, ID> jpaRepository,
            ElasticsearchRepository<ELASTIC, DOCUMENTID> elasticsearchRepository,
            Function<JPA, ELASTIC> entityToDocumentMapper,
            String methodName) {
        elasticsearchTemplate.deleteIndex(documentClass);
        elasticsearchTemplate.createIndex(documentClass);
        elasticsearchTemplate.putMapping(documentClass);

        if (jpaRepository.count() > 0) {
            reindexWithStream(jpaRepository, elasticsearchRepository,
                entityToDocumentMapper, documentClass, methodName);
        }
        log.info("Elasticsearch: Indexed all rows for " + documentClass.getSimpleName());
    }

    private <JPA, ELASTIC, ID extends Serializable, DOCUMENTID extends Serializable> void reindexWithStream(
            JpaRepository<JPA, ID> jpaRepository,
            ElasticsearchRepository<ELASTIC, DOCUMENTID> elasticsearchRepository,
            Function<JPA, ELASTIC> entityToDocumentMapper, Class entityClass, String methodName) {

        try {
            disableHibernateSecondaryCache();
            Method m = jpaRepository.getClass().getMethod(methodName);
            long total = jpaRepository.count();
            AtomicInteger index = new AtomicInteger(0);
            AtomicInteger counter = new AtomicInteger(0);
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            Stream<JPA> stream = Stream.class.cast(m.invoke(jpaRepository));
            Flux.fromStream(stream)
                .map(entityToDocumentMapper)
                .buffer(BUFFER_SIZE)
                .doOnNext(elasticsearchRepository::saveAll)
                .doOnNext(jobs ->
                    log.info("Index {} chunk #{}, {} / {}", entityClass.getSimpleName(), index.incrementAndGet(), counter.addAndGet(jobs.size()), total))
                .doOnComplete(stopWatch::stop)
                .doOnComplete(() -> log.info("Indexed {} of {} entities from {} in {} s", elasticsearchRepository.count(), jpaRepository.count(), entityClass.getSimpleName(), stopWatch.getTotalTimeSeconds()))
                .subscribe(jobs -> removeAllElementFromHibernatePrimaryCache());
        } catch (Exception e) {
            log.error("ReindexWithStream failed", e);
        }
    }

    private void disableHibernateSecondaryCache() {
        ((Session) entityManager.getDelegate()).setCacheMode(CacheMode.IGNORE);
    }

    private void removeAllElementFromHibernatePrimaryCache() {
        entityManager.clear();
    }
}
