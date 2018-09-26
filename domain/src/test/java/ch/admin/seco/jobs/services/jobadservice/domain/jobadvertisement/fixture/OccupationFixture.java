package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.WorkExperience.LESS_THAN_1_YEAR;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Occupation.Builder;

public class OccupationFixture {
    public static Builder testOccupation() {
        return testOccupationEmpty()
        .setAvamOccupationCode("avamOccupationCode")
        .setSbn3Code("sbn3Code")
        .setSbn5Code("sbn5Code")
        .setBfsCode("bfsCode")
        .setLabel("label")
        .setWorkExperience(LESS_THAN_1_YEAR)
        .setEducationCode("educationCode");
    }

    public static Builder testOccupationEmpty() {
        return new Builder();
    }
}
