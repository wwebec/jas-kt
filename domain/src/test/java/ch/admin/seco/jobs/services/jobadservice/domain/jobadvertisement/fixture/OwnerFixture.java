package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture;

import static java.lang.String.format;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Owner.Builder;

public class OwnerFixture {

    public static Builder of(JobAdvertisementId id){
        return testOwner()
                .setAccessToken(format("access-token-%s", id.getValue()))
                .setUserId(format("user-id-%s", id.getValue()));
    }

    public static Builder testOwnerEmpty() {
        return new Builder();
    }

    public static Builder testOwner() {
        return testOwnerEmpty()
                .setUserId("userId")
                .setCompanyId("companyId")
                .setAccessToken("accessToken");
    }
}
