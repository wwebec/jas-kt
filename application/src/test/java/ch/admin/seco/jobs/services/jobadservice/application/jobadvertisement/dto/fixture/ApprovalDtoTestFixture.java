package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.fixture;

import static ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementApplicationServiceForAvamTest.STELLENNUMMER_AVAM;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementIdFixture.job01;

import java.time.LocalDate;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.ApprovalDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.UpdateJobAdvertisementFromAvamDto;


public class ApprovalDtoTestFixture {

    public static ApprovalDto testApprovalDto(UpdateJobAdvertisementFromAvamDto updateJobAdvertisementFromAvamDto) {
        return new ApprovalDto(
                job01.id().getValue(),
                STELLENNUMMER_AVAM,
                LocalDate.of(2018, 1, 1),
                true,
                LocalDate.of(2018, 10, 1),
                updateJobAdvertisementFromAvamDto);
    }
}
