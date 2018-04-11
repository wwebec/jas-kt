package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils;

public class ValidationError {

    private String className;
    private String fieldName;
    private String message;

    public ValidationError(Class<?> clazz, String fieldName, String message) {
        this.className = clazz.getSimpleName();
        this.fieldName = fieldName;
        this.message = message;
    }

    public String getClassName() {
        return className;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMessage() {
        return message;
    }
}
