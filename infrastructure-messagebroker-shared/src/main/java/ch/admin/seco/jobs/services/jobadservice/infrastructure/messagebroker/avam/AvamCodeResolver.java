package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import ch.admin.seco.jobs.services.jobadservice.core.utils.MappingBuilder;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.*;

/**
 * Mapping AVAM codes with the value of the application
 * Left: AVAM
 * Right: Application
 */
public class AvamCodeResolver {

    public static final MappingBuilder<String, AvamAction> ACTIONS = new MappingBuilder<String, AvamAction>()
            .put("0", AvamAction.ANMELDUNG)
            .put("2", AvamAction.ABMELDUNG)
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
            .put("41", "fa")
            .put("42", "prs")
            .put("43", "ti")
            .put("98", "other")
            .put("99", "ns")
            .toImmutable();

    public static final MappingBuilder<String, Salutation> SALUTATIONS = new MappingBuilder<String, Salutation>()
            .put("1", Salutation.MR)
            .put("2", Salutation.MS)
            .toImmutable();

    public static final MappingBuilder<String, WorkExperience> EXPERIENCES = new MappingBuilder<String, WorkExperience>()
            .put("01", WorkExperience.NO_EXPERIENCE)
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

    public static final MappingBuilder<String, WorkForm> WORK_FORMS = new MappingBuilder<String, WorkForm>()
            .put("1", WorkForm.SUNDAY_AND_HOLIDAYS)
            .put("2", WorkForm.SHIFT_WORK)
            .put("3", WorkForm.NIGHT_WORK)
            .put("4", WorkForm.HOME_WORK)
            .toImmutable();

    public static final MappingBuilder<String, SourceSystem> SOURCE_SYSTEM = new MappingBuilder<String, SourceSystem>()
            .put("Job-Room", SourceSystem.JOBROOM)
            .put("API", SourceSystem.API)
            .put("RAV", SourceSystem.RAV)
            .put("EXTERN", SourceSystem.EXTERN)
            .toImmutable();

    public static final MappingBuilder<String, CancellationCode> CANCELLATION_CODE = new MappingBuilder<String, CancellationCode>()
            .put("1", CancellationCode.OCCUPIED_JOBCENTER)
            .put("2", CancellationCode.OCCUPIED_AGENCY)
            .put("5", CancellationCode.OCCUPIED_JOBROOM)
            .put("6", CancellationCode.OCCUPIED_OTHER)
            .put("7", CancellationCode.NOT_OCCUPIED)
            .put("0", CancellationCode.CHANGE_OR_REPOSE)
            .toImmutable();
}
