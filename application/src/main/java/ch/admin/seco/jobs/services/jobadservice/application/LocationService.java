package ch.admin.seco.jobs.services.jobadservice.application;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Location;

public interface LocationService {

    Location enrichCodes(Location location);

    boolean verifyLocation(Location location);
}
