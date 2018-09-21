package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api;

import ch.admin.seco.jobs.services.jobadservice.application.HtmlToMarkdownConverter;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.*;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateLocationDto;

import java.util.List;
import java.util.stream.Collectors;

public class ApiToNormalCreateJobAdvertisementDtoConverter {

    private final HtmlToMarkdownConverter htmlToMarkdownConverter;

    public ApiToNormalCreateJobAdvertisementDtoConverter(HtmlToMarkdownConverter htmlToMarkdownConverter) {
        this.htmlToMarkdownConverter = htmlToMarkdownConverter;
    }

    public CreateJobAdvertisementDto convert(ApiCreateJobAdvertisementDto apiCreateDto) {
        return new CreateJobAdvertisementDto(
                apiCreateDto.getTitle(),
                apiCreateDto.getDescription(),
                apiCreateDto.isReportToAvam(),
                apiCreateDto.getExternalUrl(),
                apiCreateDto.getExternalReference(),
                convertContact(apiCreateDto.getContact()),
                convertPublication(apiCreateDto.getPublication()),
                apiCreateDto.getNumberOfJobs(),
                convertCompany(apiCreateDto.getCompany()),
                convertEmployer(apiCreateDto.getEmployer()),
                convertEmployment(apiCreateDto.getEmployment()),
                convertCreateLocation(apiCreateDto.getLocation()),
                convertOccupation(apiCreateDto.getOccupation()),
                convertLanguageSkills(apiCreateDto.getLanguageSkills()),
                convertApplyChannel(apiCreateDto.getApplyChannel()),
                convertPublicContact(apiCreateDto.getPublicContact())
        );
    }

    private ContactDto convertContact(ApiContactDto apiContact) {
        if (apiContact != null) {
            return new ContactDto(
                    apiContact.getSalutation(),
                    apiContact.getFirstName(),
                    apiContact.getLastName(),
                    apiContact.getPhone(),
                    apiContact.getEmail(),
                    apiContact.getLanguageIsoCode()
            );
        }
        return null;
    }

    private PublicationDto convertPublication(ApiPublicationDto apiPublication) {
        if(apiPublication != null) {
            return new PublicationDto(
              apiPublication.getStartDate(),
              apiPublication.getEndDate(),
              apiPublication.isEuresDisplay(),
              apiPublication.isEuresAnonymous(),
              apiPublication.isPublicDisplay(),
              apiPublication.isRestrictedDisplay(),
              apiPublication.isCompanyAnonymous()
            );
        }
        return null;
    }

    private  CompanyDto convertCompany(ApiCompanyDto apiCompany) {
        if(apiCompany != null) {
            return new CompanyDto(
              apiCompany.getName(),
              apiCompany.getStreet(),
              apiCompany.getHouseNumber(),
              apiCompany.getPostalCode(),
              apiCompany.getCity(),
              apiCompany.getCountryIsoCode(),
              apiCompany.getPostOfficeBoxNumber(),
              apiCompany.getPostOfficeBoxPostalCode(),
              apiCompany.getPostOfficeBoxCity(),
              apiCompany.getPhone(),
              apiCompany.getEmail(),
              apiCompany.getWebsite(),
              apiCompany.isSurrogate()
            );
        }
        return null;
    }

    private EmployerDto convertEmployer(ApiEmployerDto apiEmployer) {
        if(apiEmployer != null) {
            return new EmployerDto(
                    apiEmployer.getName(),
                    apiEmployer.getPostalCode(),
                    apiEmployer.getCity(),
                    apiEmployer.getCountryIsoCode()
            );
        }
        return null;
    }

    private EmploymentDto convertEmployment(ApiEmploymentDto apiEmployment) {
        if(apiEmployment != null) {
            return new EmploymentDto(
                    apiEmployment.getStartDate(),
                    apiEmployment.getEndDate(),
                    apiEmployment.isShortEmployment(),
                    apiEmployment.isImmediately(),
                    apiEmployment.isPermanent(),
                    apiEmployment.getWorkloadPercentageMin(),
                    apiEmployment.getWorkloadPercentageMax(),
                    apiEmployment.getWorkForms()
            );
        }
        return null;
    }

    private CreateLocationDto convertCreateLocation(ApiCreateLocationDto apiLocation) {
        if(apiLocation != null) {
            return new CreateLocationDto(
                    apiLocation.getRemarks(),
                    apiLocation.getCity(),
                    apiLocation.getPostalCode(),
                    apiLocation.getCountryIsoCode()
            );
        }
        return null;
    }

    private OccupationDto convertOccupation(ApiOccupationDto apiOccupation) {
        if(apiOccupation != null) {
            return new OccupationDto(
                    apiOccupation.getAvamOccupationCode(),
                    apiOccupation.getWorkExperience(),
                    apiOccupation.getEducationCode()
            );
        }
        return null;
    }

    private List<LanguageSkillDto> convertLanguageSkills(List<ApiLanguageSkillDto> apiLanguageSkills) {
        if(apiLanguageSkills != null) {
            return apiLanguageSkills.stream()
                    .map(apiLanguageSkill -> new LanguageSkillDto(
                            apiLanguageSkill.getLanguageIsoCode(),
                            apiLanguageSkill.getSpokenLevel(),
                            apiLanguageSkill.getWrittenLevel()
                    ))
                    .collect(Collectors.toList());
        }
        return null;
    }

    private ApplyChannelDto convertApplyChannel(ApiApplyChannelDto apiApplyChannel) {
        if(apiApplyChannel != null) {
            return new ApplyChannelDto(
                    apiApplyChannel.getMailAddress(),
                    apiApplyChannel.getEmailAddress(),
                    apiApplyChannel.getPhoneNumber(),
                    apiApplyChannel.getFormUrl(),
                    apiApplyChannel.getAdditionalInfo()
            );
        }
        return null;
    }

    private PublicContactDto convertPublicContact(ApiPublicContactDto apiPublicContact) {
        if(apiPublicContact != null) {
            return new PublicContactDto(
                    apiPublicContact.getSalutation(),
                    apiPublicContact.getFirstName(),
                    apiPublicContact.getLastName(),
                    apiPublicContact.getPhone(),
                    apiPublicContact.getEmail()
            );
        }
        return null;
    }

}
