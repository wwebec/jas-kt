package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.fixture;

import static ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementApplicationServiceForAvamTest.STELLENNUMMER_AVAM;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementIdTestFixture.job01;
import java.time.LocalDate;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.RejectionDto;

public class RejectionDtoTestFixture {
    public static RejectionDto testRejectionDto() {
        return new RejectionDto(
                job01.id().getValue(),
                STELLENNUMMER_AVAM,
                LocalDate.of(2018, 1, 1), "code",
                "reason",
                "jobcenterid");
    }
}
