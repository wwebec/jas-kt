package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api;

public class LanguageSkillApiDto {

	private String language;
	private LanguageLevel spokenLevel;
	private LanguageLevel writtenLevel;

	public enum LanguageLevel {
		no_knowledge,
		basic_knowledge,
		good,
		very_good
	}

	protected LanguageSkillApiDto() {
		// For reflection libs
	}

	public LanguageSkillApiDto(String language, LanguageLevel spokenLevel, LanguageLevel writtenLevel) {
		this.language = language;
		this.spokenLevel = spokenLevel;
		this.writtenLevel = writtenLevel;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public LanguageLevel getSpokenLevel() {
		return spokenLevel;
	}

	public void setSpokenLevel(LanguageLevel spokenLevel) {
		this.spokenLevel = spokenLevel;
	}

	public LanguageLevel getWrittenLevel() {
		return writtenLevel;
	}

	public void setWrittenLevel(LanguageLevel writtenLevel) {
		this.writtenLevel = writtenLevel;
	}
}
