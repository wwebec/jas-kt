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
        return new PublicationDto(
                LocalDate.of(2018, 1, 1),
                LocalDate.of(2018, 3, 1),
                false,
                false,
                false,
                false, companyAnonymous);
    }
}
