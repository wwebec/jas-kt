package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller.webform;

import org.springframework.stereotype.Component;

import ch.admin.seco.jobs.services.jobadservice.application.HtmlToMarkdownConverter;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.PublicContactDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementDto;

@Component
public class JobAdvertisementFromWebAssembler {

    private final HtmlToMarkdownConverter htmlToMarkdownConverter;

    public JobAdvertisementFromWebAssembler(HtmlToMarkdownConverter htmlToMarkdownConverter) {
        this.htmlToMarkdownConverter = htmlToMarkdownConverter;
    }

    CreateJobAdvertisementDto convert(WebformCreateJobAdvertisementDto webFormCreateJobAdvertisementDto) {
        webFormCreateJobAdvertisementDto.getJobDescriptions()
                .forEach(jobDescription -> jobDescription.setDescription(this.htmlToMarkdownConverter.convert(jobDescription.getDescription())));
        return new CreateJobAdvertisementDto()
                .setApplyChannel(webFormCreateJobAdvertisementDto.getApplyChannel())
                .setCompany(webFormCreateJobAdvertisementDto.getCompany())
                .setContact(webFormCreateJobAdvertisementDto.getContact())
                .setEmployer(webFormCreateJobAdvertisementDto.getEmployer())
                .setExternalReference(webFormCreateJobAdvertisementDto.getExternalReference())
                .setLanguageSkills(webFormCreateJobAdvertisementDto.getLanguageSkills())
                .setEmployment(webFormCreateJobAdvertisementDto.getEmployment())
                .setNumberOfJobs(webFormCreateJobAdvertisementDto.getNumberOfJobs())
                .setJobDescriptions(webFormCreateJobAdvertisementDto.getJobDescriptions())
                .setPublication(webFormCreateJobAdvertisementDto.getPublication())
                .setReportToAvam(webFormCreateJobAdvertisementDto.isReportToAvam())
                .setExternalUrl(webFormCreateJobAdvertisementDto.getExternalUrl())
                .setPublicContact(convertPublicContact(webFormCreateJobAdvertisementDto.getPublicContact()))
                .setLocation(webFormCreateJobAdvertisementDto.getLocation())
                .setOccupation(webFormCreateJobAdvertisementDto.getOccupation())
                ;
    }

    private PublicContactDto convertPublicContact(WebFormPublicContactDto webFormPublicContactDto) {
        if (webFormPublicContactDto == null) {
            return null;
        }
        return new PublicContactDto()
                .setSalutation(webFormPublicContactDto.getSalutation())
                .setFirstName(webFormPublicContactDto.getFirstName())
                .setLastName(webFormPublicContactDto.getLastName())
                .setPhone(webFormPublicContactDto.getPhone())
                .setEmail(webFormPublicContactDto.getEmail());
    }
}
