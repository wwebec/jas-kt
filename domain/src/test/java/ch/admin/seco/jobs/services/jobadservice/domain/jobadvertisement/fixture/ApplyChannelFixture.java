package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.ApplyChannel.Builder;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.JobCenter;

public class ApplyChannelFixture {

    public static Builder of(JobAdvertisementId id){
        return testApplyChannel();
    }

    public static Builder testApplyChannelEmpty() {
        return new Builder();
    }

    public static Builder testApplyChannel() {
        return testApplyChannelEmpty()
            .setMailAddress("mailAddress")
            .setEmailAddress("emailAddress")
            .setPhoneNumber("phoneNumber")
            .setFormUrl("formUrl")
            .setAdditionalInfo("additionalInfo");
    }

    public static Builder testDisplayApplyChannel(JobCenter jobCenter) {
        return new Builder(jobCenter);
    }
}
