package ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.write;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementRepository;
import org.hibernate.CacheMode;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import reactor.core.publisher.Flux;

import javax.persistence.EntityManager;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class JobAdvertisementIndexerService {

    public static final String INDEX_NAME_JOB_ADVERTISEMENT = "job-advertisements";
    public static final String TYPE_JOB_ADVERTISEMENT = "job-advertisement";

    private static final Logger log = LoggerFactory.getLogger(JobAdvertisementIndexerService.class);
    private static final int BUFFER_SIZE = 100;


    private final JobAdvertisementElasticsearchRepository jobAdvertisementSearchRepository;
    private final JobAdvertisementRepository jobAdvertisementRepository;
    private final ElasticsearchTemplate elasticsearchTemplate;
    private final EntityManager entityManager;

    public JobAdvertisementIndexerService(JobAdvertisementElasticsearchRepository jobAdvertisementSearchRepository,
                                          JobAdvertisementRepository jobAdvertisementRepository,
                                          ElasticsearchTemplate elasticsearchTemplate,
                                          EntityManager entityManager) {
        this.jobAdvertisementSearchRepository = jobAdvertisementSearchRepository;
        this.jobAdvertisementRepository = jobAdvertisementRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.entityManager = entityManager;
    }

    @Async
    @Transactional(readOnly = true)
    public void reindexAll() {
        elasticsearchTemplate.createIndex(JobAdvertisementDocument.class);
        elasticsearchTemplate.putMapping(JobAdvertisementDocument.class);

        disableHibernateSecondaryCache();

        final long total = this.jobAdvertisementRepository.countPublished();
        final AtomicInteger index = new AtomicInteger(0);
        final AtomicInteger counter = new AtomicInteger(0);
        final StopWatch stopWatch = new StopWatch();

        stopWatch.start();

        Flux.fromStream(this.jobAdvertisementRepository.streamAllPublished())
                .map(JobAdvertisementDocument::new)
                .buffer(BUFFER_SIZE)
                .doOnNext(this.jobAdvertisementSearchRepository::saveAll)
                .doOnNext(jobs -> log.info("Index {} chunk #{}, {} / {}", INDEX_NAME_JOB_ADVERTISEMENT, index.incrementAndGet(), counter.addAndGet(jobs.size()), total))
                .doOnComplete(stopWatch::stop)
                .doOnComplete(() -> log.info("Indexed {} of {} entities from {} in {} s", INDEX_NAME_JOB_ADVERTISEMENT, this.jobAdvertisementSearchRepository.count(), INDEX_NAME_JOB_ADVERTISEMENT, stopWatch.getTotalTimeSeconds()))
                .subscribe(jobs -> removeAllElementFromHibernatePrimaryCache());

    }

    private void disableHibernateSecondaryCache() {
        ((Session) entityManager.getDelegate()).setCacheMode(CacheMode.IGNORE);
    }

    private void removeAllElementFromHibernatePrimaryCache() {
        entityManager.clear();
    }
}
