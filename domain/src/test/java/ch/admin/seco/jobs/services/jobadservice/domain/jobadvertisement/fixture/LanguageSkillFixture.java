package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageLevel.INTERMEDIATE;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageLevel.PROFICIENT;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageSkill.Builder;

public class LanguageSkillFixture {
    public static Builder testLanguageSkillsEmpty() {
        return new Builder();
    }

    public static Builder testLanguageSkill() {
        return testLanguageSkillsEmpty()
                .setLanguageIsoCode("de")
                .setSpokenLevel(PROFICIENT)
                .setWrittenLevel(INTERMEDIATE);
    }
}
