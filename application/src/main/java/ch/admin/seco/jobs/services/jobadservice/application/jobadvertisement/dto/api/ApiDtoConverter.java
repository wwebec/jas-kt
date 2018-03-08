package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Employment;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Location;

public class ApiDtoConverter {

    public static Employment toEmployment(JobApiDto jobApiDto) {
        return new Employment(
                jobApiDto.getStartDate(),
                jobApiDto.getEndDate(),
                jobApiDto.getDurationInDays(),
                jobApiDto.getStartsImmediately(),
                jobApiDto.getPermanent(),
                jobApiDto.getWorkingTimePercentageFrom(),
                jobApiDto.getWorkingTimePercentageTo()
        );
    }

    public static Location toLocation(LocationApiDto locationApiDto) {
        if (locationApiDto != null) {
            return new Location(
                    locationApiDto.getRemarks(),
                    locationApiDto.getCity(),
                    locationApiDto.getPostalCode(),
                    null,
                    null,
                    locationApiDto.getCantonCode(),
                    locationApiDto.getCountryIsoCode(),
                    null
            );
        }
        return null;
    }

}
