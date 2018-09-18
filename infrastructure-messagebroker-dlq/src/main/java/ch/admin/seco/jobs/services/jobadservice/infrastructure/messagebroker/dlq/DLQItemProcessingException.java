package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.dlq;

import org.springframework.messaging.Message;

class DLQItemProcessingException extends RuntimeException {

    DLQItemProcessingException(Message<?> message, Exception e) {
        super("Error handling message " + message.toString(), e);
    }
}
