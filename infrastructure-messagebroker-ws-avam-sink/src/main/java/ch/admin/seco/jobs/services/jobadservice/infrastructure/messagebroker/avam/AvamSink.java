package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;

@EnableBinding(Sink.class)
public class AvamSink {

    private final AvamWebServiceClient avamWebService;

    public AvamSink(AvamWebServiceClient avamWebService) {
        this.avamWebService = avamWebService;
    }

    @StreamListener(target = Sink.INPUT, condition = "header[event]=='JOB_ADVERTISEMENT_INSPECTING'")
    public void register(JobAdvertisement jobAdvertisement) {
        avamWebService.register(jobAdvertisement);
    }

    @StreamListener(target = Sink.INPUT, condition = "header[event]=='JOB_ADVERTISEMENT_CANCELLED'")
    public void deregister(JobAdvertisement jobAdvertisement) {
        avamWebService.deregister(jobAdvertisement);
    }
}
