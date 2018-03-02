package ch.admin.seco.jobs.services.jobadservice.application;

import ch.admin.seco.jobs.services.jobadservice.domain.profession.Profession;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionId;

public interface ProfessionService {

    Profession findById(ProfessionId professionId);

    Profession findByAvamCode(String avamCode);

}
