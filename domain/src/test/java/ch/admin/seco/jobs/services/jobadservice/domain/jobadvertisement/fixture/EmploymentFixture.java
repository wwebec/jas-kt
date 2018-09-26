package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture;

import static java.time.LocalDate.now;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Employment;

public class EmploymentFixture {

    public static Employment.Builder testEmployment() {
        return new Employment.Builder().
                setStartDate(now()).
                setEndDate(now().plusDays(31)).
                setWorkloadPercentageMin(80).
                setWorkloadPercentageMax(100);
    }
}
