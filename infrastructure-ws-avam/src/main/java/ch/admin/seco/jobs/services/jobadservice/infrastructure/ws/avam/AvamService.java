package ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;

public class AvamService {

    private static final String ACTION_CODE_ANMELDUNG = "0";
    private static final String ACTION_CODE_ABMELDUNG = "2";

    public AvamService(String endPointUrl, String username, String password) {
    }

    public void sendAnmeldung(JobAdvertisement jobAdvertisement) {
        send(jobAdvertisement, ACTION_CODE_ANMELDUNG);
    }

    public void sendAbmeldung(JobAdvertisement jobAdvertisement) {
        send(jobAdvertisement, ACTION_CODE_ABMELDUNG);
    }

    protected void send(JobAdvertisement jobAdvertisement, String actionCode) {

    }

}
