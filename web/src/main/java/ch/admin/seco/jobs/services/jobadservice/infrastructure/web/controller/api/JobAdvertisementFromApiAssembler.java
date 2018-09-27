package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller.api;

import ch.admin.seco.jobs.services.jobadservice.application.HtmlToMarkdownConverter;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.*;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateLocationDto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class JobAdvertisementFromApiAssembler {

    private final HtmlToMarkdownConverter htmlToMarkdownConverter;

    JobAdvertisementFromApiAssembler(HtmlToMarkdownConverter htmlToMarkdownConverter) {
        this.htmlToMarkdownConverter = htmlToMarkdownConverter;
    }

    CreateJobAdvertisementDto convert(ApiCreateJobAdvertisementDto apiCreateDto) {
        return new CreateJobAdvertisementDto()
                .setReportToAvam(apiCreateDto.isReportToAvam())
                .setExternalUrl(apiCreateDto.getExternalUrl())
                .setExternalReference(apiCreateDto.getExternalReference())
                .setContact(convertContact(apiCreateDto.getContact()))
                .setPublication(convertPublication(apiCreateDto.getPublication()))
                .setNumberOfJobs(apiCreateDto.getNumberOfJobs())
                .setJobDescriptions(convertJobDescriptions(apiCreateDto.getJobDescriptions()))
                .setCompany(convertCompany(apiCreateDto.getCompany()))
                .setEmployer(convertEmployer(apiCreateDto.getEmployer()))
                .setEmployment(convertEmployment(apiCreateDto.getEmployment()))
                .setLocation(convertCreateLocation(apiCreateDto.getLocation()))
                .setOccupation(convertOccupation(apiCreateDto.getOccupation()))
                .setLanguageSkills(convertLanguageSkills(apiCreateDto.getLanguageSkills()))
                .setApplyChannel(convertApplyChannel(apiCreateDto.getApplyChannel()))
                .setPublicContact(convertPublicContact(apiCreateDto.getPublicContact()));
    }

    private ContactDto convertContact(ApiContactDto apiContact) {
        if (apiContact == null) {
            return null;
        }
        return new ContactDto()
                .setSalutation(apiContact.getSalutation())
                .setFirstName(apiContact.getFirstName())
                .setLastName(apiContact.getLastName())
                .setPhone(apiContact.getPhone())
                .setEmail(apiContact.getEmail())
                .setLanguageIsoCode(apiContact.getLanguageIsoCode());
    }

    private PublicationDto convertPublication(ApiPublicationDto apiPublication) {
        if (apiPublication == null) {
            return null;
        }
        return new PublicationDto()
                .setStartDate(apiPublication.getStartDate())
                .setEndDate(apiPublication.getEndDate())
                .setEuresDisplay(apiPublication.isEuresDisplay())
                .setEuresAnonymous(apiPublication.isEuresAnonymous())
                .setPublicDisplay(apiPublication.isPublicDisplay())
                .setRestrictedDisplay(apiPublication.isRestrictedDisplay())
                .setCompanyAnonymous(apiPublication.isCompanyAnonymous());
    }

    private List<JobDescriptionDto> convertJobDescriptions(List<ApiJobDescriptionDto> apiJobDescriptions) {
        if (apiJobDescriptions == null) {
            return null;
        }
        return apiJobDescriptions.stream()
                .map(apiJobDescription -> new JobDescriptionDto()
                        .setLanguageIsoCode(apiJobDescription.getLanguageIsoCode())
                        .setTitle(apiJobDescription.getTitle())
                        .setDescription(htmlToMarkdownConverter.convert(apiJobDescription.getDescription())
                        ))
                .collect(Collectors.toList());
    }


    private CompanyDto convertCompany(ApiCompanyDto apiCompany) {
        if (apiCompany == null) {
            return null;
        }
        return new CompanyDto()
                .setName(apiCompany.getName())
                .setStreet(apiCompany.getStreet())
                .setHouseNumber(apiCompany.getHouseNumber())
                .setPostalCode(apiCompany.getPostalCode())
                .setCity(apiCompany.getCity())
                .setCountryIsoCode(apiCompany.getCountryIsoCode())
                .setPostOfficeBoxNumber(apiCompany.getPostOfficeBoxNumber())
                .setPostOfficeBoxPostalCode(apiCompany.getPostOfficeBoxPostalCode())
                .setPostOfficeBoxCity(apiCompany.getPostOfficeBoxCity())
                .setPhone(apiCompany.getPhone())
                .setEmail(apiCompany.getEmail())
                .setWebsite(apiCompany.getWebsite())
                .setSurrogate(apiCompany.isSurrogate());
    }

    private EmployerDto convertEmployer(ApiEmployerDto apiEmployer) {
        if (apiEmployer == null) {
            return null;
        }
        return new EmployerDto()
                .setName(apiEmployer.getName())
                .setPostalCode(apiEmployer.getPostalCode())
                .setCity(apiEmployer.getCity())
                .setCountryIsoCode(apiEmployer.getCountryIsoCode());
    }

    private EmploymentDto convertEmployment(ApiEmploymentDto apiEmployment) {
        if (apiEmployment == null) {
            return null;
        }
        return new EmploymentDto()
                .setStartDate(apiEmployment.getStartDate())
                .setEndDate(apiEmployment.getEndDate())
                .setShortEmployment(apiEmployment.isShortEmployment())
                .setImmediately(apiEmployment.isImmediately())
                .setPermanent(apiEmployment.isPermanent())
                .setWorkloadPercentageMin(apiEmployment.getWorkloadPercentageMin())
                .setWorkloadPercentageMax(apiEmployment.getWorkloadPercentageMax())
                .setWorkForms(apiEmployment.getWorkForms());
    }

    private CreateLocationDto convertCreateLocation(ApiCreateLocationDto apiLocation) {
        if (apiLocation == null) {
            return null;
        }
        return new CreateLocationDto()
                .setRemarks(apiLocation.getRemarks())
                .setCity(apiLocation.getCity())
                .setPostalCode(apiLocation.getPostalCode())
                .setCountryIsoCode(apiLocation.getCountryIsoCode());
    }

    private OccupationDto convertOccupation(ApiOccupationDto apiOccupation) {
        if (apiOccupation == null) {
            return null;
        }
        return new OccupationDto()
                .setAvamOccupationCode(apiOccupation.getAvamOccupationCode())
                .setWorkExperience(apiOccupation.getWorkExperience())
                .setEducationCode(apiOccupation.getEducationCode());
    }

    private List<LanguageSkillDto> convertLanguageSkills(List<ApiLanguageSkillDto> apiLanguageSkills) {
        if (apiLanguageSkills == null) {
            return null;
        }
        return apiLanguageSkills.stream()
                .map(apiLanguageSkill -> new LanguageSkillDto()
                        .setLanguageIsoCode(apiLanguageSkill.getLanguageIsoCode())
                        .setSpokenLevel(apiLanguageSkill.getSpokenLevel())
                        .setWrittenLevel(apiLanguageSkill.getWrittenLevel())
                )
                .collect(Collectors.toList());
    }

    private ApplyChannelDto convertApplyChannel(ApiApplyChannelDto apiApplyChannel) {
        if (apiApplyChannel == null) {
            return null;
        }
        return new ApplyChannelDto()
                .setMailAddress(apiApplyChannel.getMailAddress())
                .setEmailAddress(apiApplyChannel.getEmailAddress())
                .setPhoneNumber(apiApplyChannel.getPhoneNumber())
                .setFormUrl(apiApplyChannel.getFormUrl())
                .setAdditionalInfo(apiApplyChannel.getAdditionalInfo());
    }

    private PublicContactDto convertPublicContact(ApiPublicContactDto apiPublicContact) {
        if (apiPublicContact == null) {
            return null;
        }
        return new PublicContactDto()
                .setSalutation(apiPublicContact.getSalutation())
                .setFirstName(apiPublicContact.getFirstName())
                .setLastName(apiPublicContact.getLastName())
                .setPhone(apiPublicContact.getPhone())
                .setEmail(apiPublicContact.getEmail());
    }

}
