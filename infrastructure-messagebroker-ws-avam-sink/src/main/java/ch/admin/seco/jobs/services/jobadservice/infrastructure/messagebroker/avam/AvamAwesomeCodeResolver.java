package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import ch.admin.seco.jobs.services.jobadservice.core.utils.MappingBuilder;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageLevel;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Salutation;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.WorkExperience;

/**
 * Mapping AVAM codes with the value of the application
 * Left: AVAM
 * Right: Application
 */
public class AvamAwesomeCodeResolver {

    public static final MappingBuilder<String, AvamAction> ACTIONS = new MappingBuilder<String, AvamAction>()
            .put("0", AvamAction.ANMELDUNG)
            .put("2", AvamAction.ABMELDUNG)
            .toImmutable();

    public static final MappingBuilder<String, String> DRIVING_LICENSE_LEVELS = new MappingBuilder<String, String>()
            .put("01", "A")
            .put("02", "A1")
            .put("10", "B")
            .put("11", "B1")
            .put("12", "BE")
            .put("20", "C")
            .put("21", "C1")
            .put("22", "C1E")
            .put("23", "CE")
            .put("30", "D")
            .put("31", "D1")
            .put("32", "D1E")
            .put("33", "DE")
            .put("40", "F")
            .put("50", "G")
            .put("70", "M")
            .toImmutable();

    public static final MappingBuilder<String, String> CURRENCIES = new MappingBuilder<String, String>()
            .put("1", "CHF")
            .put("2", "EUR")
            .put("3", "USD")
            .toImmutable();

    public static final MappingBuilder<String, String> LANGUAGES = new MappingBuilder<String, String>()
            .put("1", "de")
            .put("2", "fr")
            .put("3", "it")
            .put("4", "rm")
            .put("5", "en")
            .put("6", "es")
            .put("7", "pt")
            .put("8", "tr")
            .put("9", "el")
            .put("10", "hu")
            .put("11", "pl")
            .put("12", "cs")
            .put("13", "sr")
            .put("14", "nl")
            .put("15", "nl")
            .put("16", "ar")
            .put("17", "he")
            .put("18", "ru")
            .put("19", "sv")
            .put("20", "ja")
            .put("21", "zh")
            .put("22", "sl")
            .put("23", "hr")
            .put("24", "da")
            .put("25", "ta")
            .put("26", "sq")
            .put("27", "ku")
            .put("28", "de-ch")
            .put("29", "sr")
            .put("30", "mk")
            .put("31", "bs")
            .put("32", "bg")
            .put("33", "no")
            .put("34", "sk")
            .put("35", "lt")
            .put("36", "th")
            .put("37", "fi")
            .put("38", "km")
            .put("39", "vi")
            .put("40", "ro")
            .toImmutable();

    public static final MappingBuilder<String, Salutation> SALUTATIONS = new MappingBuilder<String, Salutation>()
            .put("1", Salutation.MR)
            .put("2", Salutation.MS)
            .toImmutable();

    public static final MappingBuilder<String, WorkExperience> EXPERIENCES = new MappingBuilder<String, WorkExperience>()
            .put("05", WorkExperience.LESS_THAN_1_YEAR)
            .put("10", WorkExperience.MORE_THAN_1_YEAR)
            .put("20", WorkExperience.MORE_THAN_3_YEARS)
            .toImmutable();

    public static final MappingBuilder<String, LanguageLevel> LANGUAGE_LEVEL = new MappingBuilder<String, LanguageLevel>()
            .put("1", LanguageLevel.PROFICIENT)
            .put("2", LanguageLevel.INTERMEDIATE)
            .put("3", LanguageLevel.BASIC)
            .put("4", LanguageLevel.NONE)
            .toImmutable();

}
