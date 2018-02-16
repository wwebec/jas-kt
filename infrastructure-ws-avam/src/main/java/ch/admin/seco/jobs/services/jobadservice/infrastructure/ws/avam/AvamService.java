package ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.EventStore;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.wsdl.TOsteEgov;

public class AvamService {

    private static final String ACTION_CODE_ANMELDUNG = "0";
    private static final String ACTION_CODE_ABMELDUNG = "2";

    private final EventStore eventStore;

    public AvamService(EventStore eventStore, String endPointUrl, String username, String password) {
        this.eventStore = eventStore;
    }

    public void sendAnmeldung(JobAdvertisement jobAdvertisement) {
        send(jobAdvertisement, AvamAction.ANMELDUNG);
    }

    public void sendAbmeldung(JobAdvertisement jobAdvertisement) {
        send(jobAdvertisement, AvamAction.ABMELDUNG);
    }

    protected void send(JobAdvertisement jobAdvertisement, AvamAction action) {
        TOsteEgov tOsteEgov = new JobAdvertisementAssembler(eventStore).toOsteEgov(jobAdvertisement, action);
    }

}
