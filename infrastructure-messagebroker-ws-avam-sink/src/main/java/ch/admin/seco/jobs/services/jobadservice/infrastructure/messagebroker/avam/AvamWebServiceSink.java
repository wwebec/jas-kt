package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.DeregisterJobAdvertisementMessage;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.RegisterJobAdvertisementMessage;


@EnableBinding(Sink.class)
public class AvamWebServiceSink {

    private final AvamWebService avamWebService;

    public AvamWebServiceSink(AvamWebService avamWebService) {
        this.avamWebService = avamWebService;
    }

    @StreamListener(target = Sink.INPUT, condition = "header[action]==register")
    public void sendToAvam(RegisterJobAdvertisementMessage registerJobAdvertisementMessage) {
        avamWebService.register(registerJobAdvertisementMessage.getJobAdvertisement());
    }

    @StreamListener(target = Sink.INPUT, condition = "header[action]==deregister")
    public void sendToAvam(DeregisterJobAdvertisementMessage deregisterJobAdvertisementMessage) {
        avamWebService.deregister(deregisterJobAdvertisementMessage.getJobAdvertisement());
    }
}
