package ch.admin.seco.jobs.services.jobadservice.core.conditions;

import java.util.Collection;

public class Condition {

    public static <T> T notNull(T value) {
        return notNull(value, "Given value can't be null!");
    }

    public static <T> T notNull(T value, String message, Object... arguments) {
        if (value != null) {
            return value;
        }
        throw new ConditionException(message, arguments);
    }

    public static String notEmpty(String value) {
        return notEmpty(value, "Given value can't be null or empty!");
    }

    public static String notEmpty(String value, String message, Object... arguments) {
        if ((value != null) && !value.isEmpty()) {
            return value;
        }
        throw new ConditionException(message, arguments);
    }

    public static <T extends Collection> T notEmpty(T value) {
        return notEmpty(value, "Given collection can't be null or empty!");
    }

    public static <T extends Collection> T notEmpty(T value, String message, Object... arguments) {
        if ((value != null) && !value.isEmpty()) {
            return value;
        }
        throw new ConditionException(message, arguments);
    }

    public static String notBlank(String value) {
        return notBlank(value, "Given value can't be null or blank!");
    }

    public static String notBlank(String value, String message, Object... arguments) {
        if (notBlankInternal(value)) {
            return value;
        }
        throw new ConditionException(message, arguments);
    }

    public static void anyNotBlank(String message, String... values) {
        for (String value : values) {
            if (notBlankInternal(value)) {
                return;
            }
        }
        throw new ConditionException(message);
    }

    private static boolean notBlankInternal(String value) {
        if ((value != null) && !value.isEmpty()) {
            for (int i = 0; i < value.length(); i++) {
                if (!Character.isWhitespace(value.charAt(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isTrue(boolean expression) {
        return isTrue(expression, "Expression is not true!");
    }

    public static boolean isTrue(boolean expression, String message, Object... arguments) {
        if (expression) {
            return true;
        }
        throw new ConditionException(message, arguments);
    }

    public static boolean isFalse(boolean expression) {
        return isFalse(expression, "Expression is not false!");
    }

    public static boolean isFalse(boolean expression, String message, Object... arguments) {
        if (!expression) {
            return true;
        }
        throw new ConditionException(message, arguments);
    }

    public static <T> boolean contains(Collection<T> collection, T value) {
        return contains(collection, value, "Given value is not in the collection");
    }

    public static <T> boolean contains(Collection<T> collection, T value, String message, Object... arguments) {
        if (collection.contains(value)) {
            return true;
        }
        throw new ConditionException(message, arguments);
    }

    public static <T> boolean notContains(Collection<T> collection, T value) {
        return notContains(collection, value, "Given value is in the collection");
    }

    public static <T> boolean notContains(Collection<T> collection, T value, String message, Object... arguments) {
        if (!collection.contains(value)) {
            return true;
        }
        throw new ConditionException(message, arguments);
    }

}
