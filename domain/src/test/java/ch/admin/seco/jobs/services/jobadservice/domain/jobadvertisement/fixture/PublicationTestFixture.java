package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture;

import static java.time.LocalDate.now;

import java.time.LocalDate;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Publication;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Publication.Builder;

public class PublicationTestFixture {
    public static Publication testPublicationWithEndDate(LocalDate endDate) {
        return new Builder()
                .setEndDate(endDate)
                .build();
    }

    public static Publication testPublicationWithStartDate(LocalDate startDate) {
        return new Builder()
                .setStartDate(startDate)
                .setEndDate(startDate.plusMonths(1))
                .build();
    }


    public static Publication testEmptyPublication() {
        return new Publication.Builder().build();
    }

    public static Publication testPublication() {
        return testPublication(true, false);
    }

    public static Publication testPublication(boolean publicDisplay, boolean restrictedDisplay) {
        return new Publication.Builder()
                .setRestrictedDisplay(restrictedDisplay)
                .setPublicDisplay(publicDisplay)
                .setStartDate(now())
                .setEndDate(now().plusDays(5))
                .build();
    }

    public static Publication testPublicationWithAnonymousCompany() {
        return new Publication.Builder().setCompanyAnonymous(true).build();
    }
}
