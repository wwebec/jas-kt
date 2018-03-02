package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages;

import java.util.List;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.Profession;

public class RegisterJobAdvertisementMessage {

    private final JobAdvertisement jobAdvertisement;
    private final List<Profession> professions;

    public RegisterJobAdvertisementMessage(JobAdvertisement jobAdvertisement, List<Profession> professions) {
        this.jobAdvertisement = jobAdvertisement;
        this.professions = professions;
    }

    public JobAdvertisement getJobAdvertisement() {
        return jobAdvertisement;
    }

    public List<Profession> getProfessions() {
        return professions;
    }
}
