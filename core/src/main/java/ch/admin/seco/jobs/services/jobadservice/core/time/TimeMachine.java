package ch.admin.seco.jobs.services.jobadservice.core.time;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * This Class is mainly used for unit test puposes in order to manipulate the creation point (.now()) of java.time classes such as {@link LocalDateTime}.
 * <p>
 * It is important to call the TimeMachine.now() method within the productive code in order to receive a new {@link LocalDateTime} for the given manipulated date
 */
public class TimeMachine {

    private static Clock clock = Clock.systemDefaultZone();

    private static ZoneId zoneId = ZoneId.systemDefault();

    private TimeMachine() {
    }

    public static LocalDateTime now() {
        return LocalDateTime.now(getClock());
    }

    private static Clock getClock() {
        return clock;
    }

    public static void useFixedClockAt(LocalDateTime date) {
        clock = Clock.fixed(date.atZone(zoneId).toInstant(), zoneId);
    }

    public static void reset() {
        clock = Clock.systemDefaultZone();
    }
}
