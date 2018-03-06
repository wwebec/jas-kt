package ch.admin.seco.jobs.services.jobadservice.application;

import ch.admin.seco.jobs.services.jobadservice.domain.profession.Profession;

public interface ProfessionService {

    Profession findByAvamCode(String avamCode);

}
