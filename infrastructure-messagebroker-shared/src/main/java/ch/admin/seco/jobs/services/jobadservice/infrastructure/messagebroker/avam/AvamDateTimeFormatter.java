package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import static org.springframework.util.StringUtils.hasText;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AvamDateTimeFormatter {

    private static final DateTimeFormatter AVAM_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static String formatLocalDate(LocalDate localDate) {
        return (localDate != null) ? localDate.format(AVAM_DATE_FORMATTER) : null;
    }

    public static LocalDate parseToLocalDate(String date) {
        if (hasText(date)) {
            return LocalDate.parse(date, AVAM_DATE_FORMATTER);
        }
        return null;
    }
}
