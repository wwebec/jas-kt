package ch.admin.seco.jobs.services.jobadservice.core.utils;

import java.util.Collection;
import java.util.Objects;

public class CompareUtils {

    public static <T> boolean hasChanged(T current, T updated) {
        return !Objects.equals(current, updated);
    }

    public static <T> boolean hasChangedContent(Collection<T> current, Collection<T> updated) {
        if ((current == null) && (updated == null)) {
            return false;
        }
        if ((current == null) || (updated == null)) {
            return true;
        }
        return !(current.containsAll(updated) && updated.containsAll(current));
    }

}
