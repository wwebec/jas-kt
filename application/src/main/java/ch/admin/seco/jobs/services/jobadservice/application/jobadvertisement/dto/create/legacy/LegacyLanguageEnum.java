package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import ch.admin.seco.jobs.services.jobadservice.core.utils.MappingBuilder;

/**
 * The language codes rely on the ISO 639-1 norm, with an exception for swiss german (de-ch).
 */
public enum LegacyLanguageEnum {
    DE,
    FR,
    IT,
    RM,
    EN,
    ES,
    PT,
    TR,
    EL,
    HU,
    PL,
    CS,
    SR,
    NL,
    AR,
    HE,
    RU,
    SV,
    JA,
    ZH,
    SL,
    HR,
    DA,
    TA,
    SQ,
    KU,
    DE_CH,
    MK,
    BS,
    BG,
    NO,
    SK,
    LT,
    TH,
    FI,
    KM,
    VI,
    RO;

    public static final MappingBuilder<LegacyLanguageEnum, String> MAP_JSON_FORMAT = new MappingBuilder<LegacyLanguageEnum, String>()
            .put(LegacyLanguageEnum.DE, "de")
            .put(LegacyLanguageEnum.FR, "fr")
            .put(LegacyLanguageEnum.IT, "it")
            .put(LegacyLanguageEnum.RM, "rm")
            .put(LegacyLanguageEnum.EN, "en")
            .put(LegacyLanguageEnum.ES, "es")
            .put(LegacyLanguageEnum.PT, "pt")
            .put(LegacyLanguageEnum.TR, "tr")
            .put(LegacyLanguageEnum.EL, "el")
            .put(LegacyLanguageEnum.HU, "hu")
            .put(LegacyLanguageEnum.PL, "pl")
            .put(LegacyLanguageEnum.CS, "cs")
            .put(LegacyLanguageEnum.NL, "nl")
            .put(LegacyLanguageEnum.AR, "ar")
            .put(LegacyLanguageEnum.HE, "he")
            .put(LegacyLanguageEnum.RU, "ru")
            .put(LegacyLanguageEnum.SV, "sv")
            .put(LegacyLanguageEnum.JA, "ja")
            .put(LegacyLanguageEnum.ZH, "zh")
            .put(LegacyLanguageEnum.SL, "sl")
            .put(LegacyLanguageEnum.HR, "hr")
            .put(LegacyLanguageEnum.DA, "da")
            .put(LegacyLanguageEnum.TA, "ta")
            .put(LegacyLanguageEnum.SQ, "sq")
            .put(LegacyLanguageEnum.KU, "ku")
            .put(LegacyLanguageEnum.DE_CH, "de-ch")
            .put(LegacyLanguageEnum.SR, "sr")
            .put(LegacyLanguageEnum.MK, "mk")
            .put(LegacyLanguageEnum.BS, "bs")
            .put(LegacyLanguageEnum.BG, "bg")
            .put(LegacyLanguageEnum.NO, "no")
            .put(LegacyLanguageEnum.SK, "sk")
            .put(LegacyLanguageEnum.LT, "lt")
            .put(LegacyLanguageEnum.TH, "th")
            .put(LegacyLanguageEnum.FI, "fi")
            .put(LegacyLanguageEnum.KM, "km")
            .put(LegacyLanguageEnum.VI, "vi")
            .put(LegacyLanguageEnum.RO, "ro")
            .toImmutable();

    @JsonCreator
    public static LegacyLanguageEnum fromValue(String value) {
        return MAP_JSON_FORMAT.getLeft(value);
    }

    @JsonValue
    public String toValue() {
        return MAP_JSON_FORMAT.getRight(this);
    }
}
