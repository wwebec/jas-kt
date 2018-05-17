package ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.write.apiuser;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ApiUserElasticsearchRepository extends ElasticsearchRepository<ApiUserDocument, String> {
}
