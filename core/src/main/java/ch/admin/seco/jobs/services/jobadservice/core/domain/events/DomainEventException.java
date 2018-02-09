package ch.admin.seco.jobs.services.jobadservice.core.domain.events;

public class DomainEventException extends RuntimeException {

    public DomainEventException(String message) {
        super(message);
    }

    public DomainEventException(String message, Throwable cause) {
        super(message, cause);
    }

    public DomainEventException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

}
