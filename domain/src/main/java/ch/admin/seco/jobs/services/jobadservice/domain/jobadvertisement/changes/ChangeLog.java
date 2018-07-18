package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.changes;

import java.util.*;

public class ChangeLog {

    private List<ChangeLogEntry> entries = new ArrayList<>();

    public ChangeLog() {
    }

    public void add(String field, Object oldValue, Object newValue) {
        entries.add(new ChangeLogEntry(field, oldValue, newValue));
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public List<ChangeLogEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    @Override
    public String toString() {
        return "ChangeLog{" +
                "entries=" + entries +
                '}';
    }
}
