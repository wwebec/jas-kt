package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture;

import static java.time.LocalDate.now;

import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Employment;

public class EmploymentTestFixture {
    public static Employment testEmployment() {
        return testEmployment(false);
    }

    public static Employment testEmploymentPermanent() {
        return testEmployment(true);
    }

    public static Employment testEmployment(boolean permanent) {
        return new Employment.Builder()
                .setStartDate(now())
                .setEndDate(now().plusDays(31))
                .setShortEmployment(true)
                .setImmediately(false)
                .setPermanent(permanent)
                .setWorkloadPercentageMin(80)
                .setWorkloadPercentageMax(100)
                .build();
    }
}
