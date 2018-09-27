package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.fixture;

import java.time.LocalDate;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.PublicationDto;

public class PublicationDtoTestFixture {

    public static PublicationDto testPublicationDto() {
        return testPublicationDto(false);
    }

    public static PublicationDto testPublicationDtoWithCompanyAnonymous() {
        return testPublicationDto(true);
    }

    private static PublicationDto testPublicationDto(boolean companyAnonymous) {
        return new PublicationDto()
                .setStartDate(LocalDate.of(2018, 1, 1))
                .setEndDate(LocalDate.of(2018, 3, 1))
                .setEuresDisplay(false)
                .setEuresAnonymous(false)
                .setPublicDisplay(false)
                .setRestrictedDisplay(false)
                .setCompanyAnonymous(companyAnonymous);
    }
}
