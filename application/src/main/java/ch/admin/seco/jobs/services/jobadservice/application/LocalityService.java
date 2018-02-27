package ch.admin.seco.jobs.services.jobadservice.application;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Locality;

public interface LocalityService {

    Locality enrichCodes(Locality locality);

}
