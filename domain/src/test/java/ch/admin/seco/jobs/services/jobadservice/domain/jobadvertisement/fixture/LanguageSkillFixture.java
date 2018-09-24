package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageLevel.NONE;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageSkill.Builder;

public class LanguageSkillFixture {
    public static Builder testLanguageSkillsEmpty() {
        return new Builder();
    }

    public static Builder testLanguageSkill() {
        return testLanguageSkillsEmpty()
                .setLanguageIsoCode("de")
                .setSpokenLevel(NONE)
                .setWrittenLevel(NONE);
    }
}
