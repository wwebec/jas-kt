package ch.admin.seco.jobs.services.jobadservice.core.conditions;

public class ConditionException extends RuntimeException {

    public ConditionException(String message, Object... arguments) {
        super(String.format(message, arguments));
    }

    public ConditionException(String message) {
        super(message);
    }

}
