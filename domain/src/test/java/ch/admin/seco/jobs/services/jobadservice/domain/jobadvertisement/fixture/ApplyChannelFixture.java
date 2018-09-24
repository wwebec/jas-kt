package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.ApplyChannel.Builder;

public class ApplyChannelFixture {
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
}
