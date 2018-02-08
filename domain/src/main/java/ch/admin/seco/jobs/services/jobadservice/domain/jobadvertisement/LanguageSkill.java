package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

public class LanguageSkill {

    private String languageCode;
    private LanguageLevel spokenLevel;
    private LanguageLevel writtenLevel;
    private boolean motherTongue = false; // TODO check if it is part of the LanguageLevel, or removable
    private boolean languageStayRequired = false; // TODO check if it is removable

}
