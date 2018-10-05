package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.fixture;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.ApplyChannelDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.CompanyDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.ContactDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.EmploymentDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.LanguageSkillDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.OccupationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.PublicationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateLocationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.UpdateJobAdvertisementFromAvamDto;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobContent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobDescription;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Location;

public class UpdateJobAdvertisementFromAvamDtoTestFixture {

    public static UpdateJobAdvertisementFromAvamDto testUpdateJobAdvertisementFromAvamDto(JobAdvertisement jobAdvertisement) {
        JobContent jobContent = jobAdvertisement.getJobContent();
        JobDescription firstDescription = jobContent.getJobDescriptions().get(0);
        Location location = jobContent.getLocation();
        CreateLocationDto locationDto = testLocationDto(location);
        return new UpdateJobAdvertisementFromAvamDto(
                jobAdvertisement.getStellennummerAvam(),
                firstDescription.getTitle(),
                firstDescription.getDescription(),
                firstDescription.getLanguage().getLanguage(),
                jobContent.getNumberOfJobs(),
                jobAdvertisement.isReportingObligation(),
                jobAdvertisement.getReportingObligationEndDate(),
                jobAdvertisement.getJobCenterCode(),
                jobAdvertisement.getApprovalDate(),
                EmploymentDto.toDto(jobContent.getEmployment()),
                ApplyChannelDto.toDto(jobContent.getApplyChannel()), // This is only for test purpose. Generally only the displayApplyChannel is converted to ApplyChannelDto
                CompanyDto.toDto(jobContent.getCompany()), // This is only for test purpose. Generally only the displayCompany is converted to CompanyDto
                ContactDto.toDto(jobAdvertisement.getContact()),
                locationDto,
                OccupationDto.toDto(jobContent.getOccupations()),
                LanguageSkillDto.toDto(jobContent.getLanguageSkills()),
                PublicationDto.toDto(jobAdvertisement.getPublication())
        );
    }

    private static CreateLocationDto testLocationDto(Location location) {
        return new CreateLocationDto()
                .setRemarks(location.getRemarks())
                .setCity(location.getCity())
                .setPostalCode(location.getPostalCode())
                .setCountryIsoCode(location.getCountryIsoCode());
    }
}
