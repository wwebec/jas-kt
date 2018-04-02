package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.ApplyChannelDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.CompanyDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.ContactDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.EmploymentDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.JobDescriptionDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.LanguageSkillDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.OccupationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.PublicContactDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.PublicationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateLocationDto;
import ch.admin.seco.jobs.services.jobadservice.core.utils.MappingBuilder;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageLevel;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.WorkExperience;

public class LegacyToJobAdvertisementConverter {

	private static final MappingBuilder<LegacyExperienceEnum, WorkExperience> EXPERIENCE_MAPPING = new MappingBuilder<LegacyExperienceEnum, WorkExperience>()
			.put(LegacyExperienceEnum.NO_EXPERIENCE, null)
			.put(LegacyExperienceEnum.LESS_THAN_1_YEAR, WorkExperience.LESS_THAN_1_YEAR)
			.put(LegacyExperienceEnum.BETWEEN_1_AND_3_YEARS, WorkExperience.MORE_THAN_1_YEAR)
			.put(LegacyExperienceEnum.MORE_THAN_3_YEARS, WorkExperience.MORE_THAN_3_YEARS)
			.toImmutable();

	private static final MappingBuilder<LegacyLanguageLevelEnum, LanguageLevel> LANGUAGE_LEVEL_MAPPING = new MappingBuilder<LegacyLanguageLevelEnum, LanguageLevel>()
			.put(LegacyLanguageLevelEnum.NONE, LanguageLevel.NONE)
			.put(LegacyLanguageLevelEnum.BASIC, LanguageLevel.BASIC)
			.put(LegacyLanguageLevelEnum.INTERMEDIATE, LanguageLevel.INTERMEDIATE)
			.put(LegacyLanguageLevelEnum.PROFICIENT, LanguageLevel.PROFICIENT)
			.toImmutable();

	public static CreateJobAdvertisementDto convert(LegacyJobAdvertisementDto legacyJobAdvertisementDto) {
		String language = legacyJobAdvertisementDto.getLocale().getLanguage();
		return new CreateJobAdvertisementDto(
				false,
				null,
				convertContactDto(legacyJobAdvertisementDto.getContact(), language),
				convertPublicationDto(legacyJobAdvertisementDto),
				convertJobDescriptions(legacyJobAdvertisementDto, language),
				convertCompanyDto(legacyJobAdvertisementDto.getCompany()),
				null,
				convertEmploymentDto(legacyJobAdvertisementDto.getJob()),
				convertLocationDto(legacyJobAdvertisementDto.getJob().getLocation()),
				convertOccupationDto(legacyJobAdvertisementDto.getJob().getOccupation()),
				convertLanguageSkills(legacyJobAdvertisementDto.getJob().getLanguageSkills()),
				convertApplyChannelDto(legacyJobAdvertisementDto.getApplication()),
				convertPublicContactDto(legacyJobAdvertisementDto)
		);
	}

	private static PublicContactDto convertPublicContactDto(LegacyJobAdvertisementDto legacyJobAdvertisementDto) {
		LegacyContactDto contact = legacyJobAdvertisementDto.getContact();
		return new PublicContactDto(
				contact.getSalutation(),
				contact.getFirstName(),
				contact.getLastName(),
				contact.getPhoneNumber(),
				contact.getEmail()
		);
	}

	private static ApplyChannelDto convertApplyChannelDto(LegacyApplicationDto application) {
		return new ApplyChannelDto(
				null,
				application.getEmail(),
				application.getPhoneNumber(),
				application.getUrl(),
				application.getAdditionalInfo()
		);
	}

	private static List<LanguageSkillDto> convertLanguageSkills(List<LegacyLanguageSkillDto> languageSkills) {
		return languageSkills.stream()
				.map(legacyLanguageSkillDto -> new LanguageSkillDto(
						legacyLanguageSkillDto.getCode().name(),
						LANGUAGE_LEVEL_MAPPING.getRight(legacyLanguageSkillDto.getSpokenLevel()),
						LANGUAGE_LEVEL_MAPPING.getRight(legacyLanguageSkillDto.getWrittenLevel())
						))
				.collect(toList());
	}

	private static OccupationDto convertOccupationDto(LegacyOccupationDto occupation) {
		return new OccupationDto(
				occupation.getAvamOccupation(),
				EXPERIENCE_MAPPING.getRight(occupation.getExperience()),
				occupation.getDegree().name()
		);
	}

	private static CreateLocationDto convertLocationDto(LegacyLocationDto legacyLocationDto) {
		return new CreateLocationDto(
				legacyLocationDto.getAdditionalDetails(),
				legacyLocationDto.getCity(),
				legacyLocationDto.getZipCode(),
				legacyLocationDto.getCountryCode(),
				legacyLocationDto.getCommunalCode()
		);
	}

	private static EmploymentDto convertEmploymentDto(LegacyJobDto legacyJobDto) {
		return new EmploymentDto(
				legacyJobDto.getStartDate(),
				legacyJobDto.getEndDate(),
				false,
				legacyJobDto.isStartsImmediately(),
				legacyJobDto.isPermanent(),
				legacyJobDto.getWorkingTimePercentageMin(),
				legacyJobDto.getWorkingTimePercentageMax(),
				null
		);
	}

	private static CompanyDto convertCompanyDto(LegacyCompanyDto company) {
		return new CompanyDto(
				company.getName(),
				company.getStreet(),
				company.getHouseNumber(),
				company.getZipCode(),
				company.getCity(),
				company.getCountryCode(),
				company.getPostboxNumber(),
				company.getPostboxZipCode(),
				company.getPostboxCity(),
				null,
				null,
				null,
				false
		);
	}

	private static List<JobDescriptionDto> convertJobDescriptions(LegacyJobAdvertisementDto legacyJobAdvertisementDto, String language) {
		return Arrays.asList(new JobDescriptionDto(language,
				legacyJobAdvertisementDto.getJob().getTitle(),
				legacyJobAdvertisementDto.getJob().getDescription()));
	}

	private static PublicationDto convertPublicationDto(LegacyJobAdvertisementDto legacyJobAdvertisementDto) {
		return new PublicationDto(
				legacyJobAdvertisementDto.getJob().getStartDate(),
				legacyJobAdvertisementDto.getJob().getEndDate(),
				legacyJobAdvertisementDto.getPublication().isEures(),
				false,
				false,
				false,
				false,
				false
		);
	}

	private static ContactDto convertContactDto(LegacyContactDto contact, String language) {
		return new ContactDto(
				contact.getSalutation(),
				contact.getFirstName(),
				contact.getLastName(),
				contact.getPhoneNumber(),
				contact.getEmail(),
				language
		);
	}
}
