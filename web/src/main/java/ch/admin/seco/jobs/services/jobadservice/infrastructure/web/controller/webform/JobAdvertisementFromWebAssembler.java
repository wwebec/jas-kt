package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller.webform;

import org.springframework.stereotype.Component;

import ch.admin.seco.jobs.services.jobadservice.application.HtmlToMarkdownConverter;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementDto;

@Component
public class JobAdvertisementFromWebAssembler {

    private final HtmlToMarkdownConverter htmlToMarkdownConverter;

    public JobAdvertisementFromWebAssembler(HtmlToMarkdownConverter htmlToMarkdownConverter) {
        this.htmlToMarkdownConverter = htmlToMarkdownConverter;
    }

    CreateJobAdvertisementDto convert(WebformCreateJobAdvertisementDto createJobAdvertisementFromWebDto) {
        createJobAdvertisementFromWebDto.getJobDescriptions()
                .forEach(jobDescription -> jobDescription.setDescription(this.htmlToMarkdownConverter.convert(jobDescription.getDescription())));
        return new CreateJobAdvertisementDto()
                .setApplyChannel(createJobAdvertisementFromWebDto.getApplyChannel())
                .setCompany(createJobAdvertisementFromWebDto.getCompany())
                .setContact(createJobAdvertisementFromWebDto.getContact())
                .setEmployer(createJobAdvertisementFromWebDto.getEmployer())
                .setExternalReference(createJobAdvertisementFromWebDto.getExternalReference())
                .setLanguageSkills(createJobAdvertisementFromWebDto.getLanguageSkills())
                .setEmployment(createJobAdvertisementFromWebDto.getEmployment())
                .setNumberOfJobs(createJobAdvertisementFromWebDto.getNumberOfJobs())
                .setJobDescriptions(createJobAdvertisementFromWebDto.getJobDescriptions())
                .setPublication(createJobAdvertisementFromWebDto.getPublication())
                .setReportToAvam(createJobAdvertisementFromWebDto.isReportToAvam())
                .setExternalUrl(createJobAdvertisementFromWebDto.getExternalUrl())
                .setPublicContact(createJobAdvertisementFromWebDto.getPublicContact())
                .setLocation(createJobAdvertisementFromWebDto.getLocation())
                .setOccupation(createJobAdvertisementFromWebDto.getOccupation())
                ;
    }
}
