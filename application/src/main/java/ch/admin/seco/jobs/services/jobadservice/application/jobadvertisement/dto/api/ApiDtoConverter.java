package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api;

import ch.admin.seco.jobs.services.jobadservice.core.utils.MappingBuilder;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.*;

import java.util.List;
import java.util.stream.Collectors;

public class ApiDtoConverter {

    private static final MappingBuilder<LanguageSkillApiDto.LanguageLevel, LanguageLevel> LANGUAGE_SKILLS = new MappingBuilder<LanguageSkillApiDto.LanguageLevel, LanguageLevel>()
            .put(LanguageSkillApiDto.LanguageLevel.no_knowledge, LanguageLevel.NONE)
            .put(LanguageSkillApiDto.LanguageLevel.basic_knowledge, LanguageLevel.BASIC)
            .put(LanguageSkillApiDto.LanguageLevel.good, LanguageLevel.INTERMEDIATE)
            .put(LanguageSkillApiDto.LanguageLevel.very_good, LanguageLevel.PROFICIENT)
            .toImmutable();

    private static final MappingBuilder<ContactApiDto.Title, Salutation> SALUTATIONS = new MappingBuilder<ContactApiDto.Title, Salutation>()
            .put(ContactApiDto.Title.mister, Salutation.MR)
            .put(ContactApiDto.Title.madam, Salutation.MS)
            .toImmutable();

    public static Employment toEmployment(JobApiDto job) {
        return new Employment(
                job.getStartDate(),
                job.getEndDate(),
                job.getDurationInDays(),
                job.getStartsImmediately(),
                job.getPermanent(),
                job.getWorkingTimePercentageFrom(),
                job.getWorkingTimePercentageTo()
        );
    }

    public static ApplyChannel toApplyChannel(CreateJobAdvertisementApiDto createJobAdvertisementApiDto) {
        String mailAddress = getMailAddressFromApi(createJobAdvertisementApiDto);
        String emailAddress = createJobAdvertisementApiDto.getContact() != null
                ? createJobAdvertisementApiDto.getContact().getEmail() : null;
        String phoneNumber = createJobAdvertisementApiDto.getContact() != null
                ? createJobAdvertisementApiDto.getContact().getPhoneNumber() : null;

        return new ApplyChannel(
                mailAddress,
                emailAddress,
                phoneNumber,
                createJobAdvertisementApiDto.getApplicationUrl(),
                null
        );
    }

    public static Company toCompany(CompanyApiDto companyApiDto) {
        if (companyApiDto != null) {
            return new Company.Builder()
                    .setName(companyApiDto.getName())
                    .setStreet(companyApiDto.getStreet())
                    .setHouseNumber(companyApiDto.getHouseNumber())
                    .setZipCode(companyApiDto.getPostalCode())
                    .setCity(companyApiDto.getLocality())
                    .setCountryIsoCode(companyApiDto.getCountryCode())
                    .setPostOfficeBoxNumber(companyApiDto.getPostbox().getNumber())
                    .setPostOfficeBoxZipCode(companyApiDto.getPostbox().getPostalCode())
                    .setPostOfficeBoxCity(companyApiDto.getPostbox().getLocality())
                    .setPhone(companyApiDto.getPhoneNumber())
                    .setEmail(companyApiDto.getEmail())
                    .setWebsite(companyApiDto.getWebsite())
                    .build();
        }
        return null;
    }

    public static Contact toContact(ContactApiDto contactApiDto) {
        if (contactApiDto != null) {
            return new Contact(
                    SALUTATIONS.getRight(contactApiDto.getTitle()),
                    contactApiDto.getFirstName(),
                    contactApiDto.getLastName(),
                    contactApiDto.getPhoneNumber(),
                    contactApiDto.getEmail()
            );
        }
        return null;
    }

    public static Locality toLocality(LocationApiDto localityDto) {
        if (localityDto != null) {
            return new Locality(
                    localityDto.getAdditionalDetails(),
                    localityDto.getLocality(),
                    localityDto.getPostalCode(),
                    localityDto.getCountryCode()
            );
        }
        return null;
    }

    public static List<LanguageSkill> toLanguageSkills(List<LanguageSkillApiDto> languageSkillApiDtos) {
        if (languageSkillApiDtos != null) {
            return languageSkillApiDtos.stream()
                    .map(languageSkillApiDto -> new LanguageSkill(
                            languageSkillApiDto.getLanguage(),
                            LANGUAGE_SKILLS.getRight(languageSkillApiDto.getSpokenLevel()),
                            LANGUAGE_SKILLS.getRight(languageSkillApiDto.getWrittenLevel())
                    ))
                    .collect(Collectors.toList());
        }
        return null;
    }

    private static String getMailAddressFromApi(CreateJobAdvertisementApiDto createJobAdvertisementApiDto) {
        CompanyApiDto company = createJobAdvertisementApiDto.getCompany();
        return String.join(", ", company.getName(), company.getStreet(), company.getPostalCode(), company.getLocality());
    }

}
