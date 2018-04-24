package ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.write;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface JobAdvertisementElasticsearchRepository extends ElasticsearchRepository<JobAdvertisementDocument, String> {
}
