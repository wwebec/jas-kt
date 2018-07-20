package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.changes;

public class ChangeLogEntry {

    private String field;

    private Object oldValue;

    private Object newValue;

    public ChangeLogEntry(String field, Object oldValue, Object newValue) {
        this.field = field;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getField() {
        return field;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }

    @Override
    public String toString() {
        return "ChangeLogEntry{" +
                "field='" + field + '\'' +
                ", oldValue=" + oldValue +
                ", newValue=" + newValue +
                '}';
    }
}
