package ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.write.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

import static ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.write.ElasticsearchIndexService.INDEX_NAME_JOB_ADVERTISEMENT;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.write.ElasticsearchIndexService.TYPE_JOB_ADVERTISEMENT;

@Document(indexName = INDEX_NAME_JOB_ADVERTISEMENT, type = TYPE_JOB_ADVERTISEMENT)
@Mapping(mappingPath = "config/elasticsearch/mappings/job-advertisement.json")
@Setting(settingPath = "config/elasticsearch/settings/folding-analyzer.json")
public class JobAdvertisementDocument {
    @Id
    private String id;

    private JobAdvertisement jobAdvertisement;

    protected JobAdvertisementDocument() {
    }

    public JobAdvertisementDocument(JobAdvertisement jobAdvertisement) {
        this.jobAdvertisement = jobAdvertisement;
        this.id = jobAdvertisement.getId().getValue();
    }

    public String getId() {
        return id;
    }

    public JobAdvertisement getJobAdvertisement() {
        return jobAdvertisement;
    }
}
