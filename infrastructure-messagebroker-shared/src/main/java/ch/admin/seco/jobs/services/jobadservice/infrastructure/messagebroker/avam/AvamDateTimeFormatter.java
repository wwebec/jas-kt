package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import static org.springframework.util.StringUtils.hasText;

public class AvamDateTimeFormatter {

    private static final DateTimeFormatter FORMAT_AVAM_DATE_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd")
            .toFormatter();

    private static final DateTimeFormatter PARSE_AVAM_DATE_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd-HH.mm.ss")
            .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
            .toFormatter();

    public static String formatLocalDate(LocalDate localDate) {
        return (localDate != null) ? localDate.format(FORMAT_AVAM_DATE_FORMATTER) + "-00.00.00.0" : null;
    }

    public static LocalDate parseToLocalDate(String date) {
        if (hasText(date)) {
            return LocalDate.parse(date, PARSE_AVAM_DATE_FORMATTER);
        }
        return null;
    }
}
