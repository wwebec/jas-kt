package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.CancellationCode.OCCUPIED_JOBCENTER;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.CREATED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem.JOBROOM;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.ContactFixture.testContact;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementIdFixture.job01;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobContentFixture.testJobContent;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.OwnerFixture.testOwner;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.PublicationFixture.testPublicationEmpty;
import static java.time.LocalDate.now;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement.Builder;

public class JobAdvertisementFixture {

    public static Builder testJobAdvertisementEmpty() {
        return new Builder();
    }

    public static Builder testJobAdvertisement() {
        return testJobAdvertisementEmpty()
                .setId(job01.id())
                .setStatus(CREATED)
                .setSourceSystem(JOBROOM)
                .setExternalReference("externalReference")
                .setStellennummerEgov(job01.name())
                .setStellennummerAvam("stellennummerAvam")
                .setFingerprint("fingerprint")
                .setReportingObligationEndDate(now().plusWeeks(4))
                .setJobCenterCode("jobCenterCode")
                .setApprovalDate(now().plusWeeks(3))
                .setRejectionDate(now().plusWeeks(2))
                .setRejectionCode("rejectionCode")
                .setRejectionReason("rejectionReason")
                .setCancellationDate(now().plusWeeks(1))
                .setCancellationCode(OCCUPIED_JOBCENTER)
                .setJobContent(testJobContent().build())
                .setOwner(testOwner().build())
                .setContact(testContact().build())
                .setPublication(testPublicationEmpty().build());
    }
}
