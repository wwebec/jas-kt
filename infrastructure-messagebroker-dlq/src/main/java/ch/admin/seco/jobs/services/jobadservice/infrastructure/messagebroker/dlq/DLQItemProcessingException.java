package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.dlq;

import org.springframework.messaging.Message;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;

class DLQItemProcessingException extends RuntimeException {

    DLQItemProcessingException(Message<JobAdvertisement> message, Exception e) {
        super("Error handling message " + message.toString(),e);
    }
}
