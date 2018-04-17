package ch.admin.seco.jobs.services.jobadservice.core.validations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Violations {

    private List<String> messages = new ArrayList<>();

    private void add(String message, Object... arguments) {
        messages.add(formatMessage(message, arguments));
    }

    public void addIfTrue(boolean expression, String message, Object... arguments) {
        if (expression) {
            add(message, arguments);
        }
    }

    public void addIfFalse(boolean expression, String message, Object... arguments) {
        addIfTrue(!expression, message, arguments);
    }

    public void addIfNull(Object value, String message, Object... arguments) {
        addIfTrue(value == null, message, arguments);
    }

    public void addIfNotNull(Object value, String message, Object... arguments) {
        addIfFalse(value == null, message, arguments);
    }

    public void addIfEmpty(String value, String message, Object... arguments) {
        addIfTrue((value == null) || value.isEmpty(), message, arguments);
    }

    public void addIfEmpty(Collection value, String message, Object... arguments) {
        addIfTrue((value == null) || value.isEmpty(), message, arguments);
    }

    public void addIfBlank(String value, String message, Object... arguments) {
        if ((value == null) || value.isEmpty()) {
            add(message, arguments);
        } else {
            boolean expression = value.chars()
                    .mapToObj(i -> (char) i)
                    .allMatch(Character::isWhitespace);
            addIfTrue(expression, message, arguments);
        }
    }

    public List<String> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    public int getCount() {
        return messages.size();
    }

    public boolean isEmpty() {
        return messages.isEmpty();
    }

    @Override
    public String toString() {
        return messages.toString();
    }

    private String formatMessage(String message, Object... arguments) {
        if ((arguments == null) || (arguments.length == 0)) {
            return message;
        }
        return String.format(message, arguments);
    }

}
