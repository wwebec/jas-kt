package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.*;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementFromAvamDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateLocationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.ApprovalDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.CancellationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.RejectionDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.UpdateJobAdvertisementFromAvamDto;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Salutation;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.WorkForm;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils.WorkingTimePercentage;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.source.WSArbeitsformArray;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.source.WSOsteEgov;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.AvamCodeResolver.*;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.AvamDateTimeFormatter.parseToLocalDate;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.util.StringUtils.hasText;
import static org.springframework.util.StringUtils.trimAllWhitespace;

public class JobAdvertisementFromAvamAssembler {

    private static final Logger LOG = LoggerFactory.getLogger(JobAdvertisementFromAvamAssembler.class);
    private static final EmailValidator emailValidator = new EmailValidator();

    private static boolean safeBoolean(Boolean value, boolean defaultValue) {
        return (value != null) ? value.booleanValue() : defaultValue;
    }

    private static String safeTrimOrNull(String value) {
        return (isNotBlank(value)) ? value.trim() : null;
    }

    CreateJobAdvertisementFromAvamDto createCreateJobAdvertisementAvamDto(WSOsteEgov avamJobAdvertisement) {
        return new CreateJobAdvertisementFromAvamDto(
                safeTrimOrNull(avamJobAdvertisement.getStellennummerAvam()),
                avamJobAdvertisement.getBezeichnung(),
                avamJobAdvertisement.getBeschreibung(),
                "de", // Not defined in this AVAM version
                avamJobAdvertisement.getGleicheOste(),
                avamJobAdvertisement.isMeldepflicht(),
                parseToLocalDate(avamJobAdvertisement.getSperrfrist()),
                avamJobAdvertisement.getArbeitsamtBereich(),
                parseToLocalDate(avamJobAdvertisement.getAnmeldeDatum()),
                createEmploymentDto(avamJobAdvertisement),
                createApplyChannelDto(avamJobAdvertisement),
                createCompanyDto(avamJobAdvertisement),
                createContactDto(avamJobAdvertisement),
                createCreateLocationDto(avamJobAdvertisement),
                createOccupationDtos(avamJobAdvertisement),
                createLanguageSkillDtos(avamJobAdvertisement),
                createPublicationDto(avamJobAdvertisement)
        );
    }

    ApprovalDto createApprovaldDto(WSOsteEgov avamJobAdvertisement) {
        return new ApprovalDto(
                safeTrimOrNull(avamJobAdvertisement.getStellennummerEgov()),
                safeTrimOrNull(avamJobAdvertisement.getStellennummerAvam()),
                parseToLocalDate(avamJobAdvertisement.getAnmeldeDatum()),
                avamJobAdvertisement.isMeldepflicht(),
                parseToLocalDate(avamJobAdvertisement.getSperrfrist()),
                new UpdateJobAdvertisementFromAvamDto(
                        safeTrimOrNull(avamJobAdvertisement.getStellennummerAvam()),
                        avamJobAdvertisement.getBezeichnung(),
                        avamJobAdvertisement.getBeschreibung(),
                        avamJobAdvertisement.getGleicheOste(),
                        avamJobAdvertisement.isMeldepflicht(),
                        parseToLocalDate(avamJobAdvertisement.getSperrfrist()),
                        avamJobAdvertisement.getArbeitsamtBereich(),
                        parseToLocalDate(avamJobAdvertisement.getAnmeldeDatum()),
                        createEmploymentDto(avamJobAdvertisement),
                        createApplyChannelDto(avamJobAdvertisement),
                        createCompanyDto(avamJobAdvertisement),
                        createContactDto(avamJobAdvertisement),
                        createCreateLocationDto(avamJobAdvertisement),
                        createOccupationDtos(avamJobAdvertisement),
                        createLanguageSkillDtos(avamJobAdvertisement),
                        createPublicationDto(avamJobAdvertisement)
                )
        );
    }

    RejectionDto createRejectionDto(WSOsteEgov avamJobAdvertisement) {
        return new RejectionDto(
                safeTrimOrNull(avamJobAdvertisement.getStellennummerEgov()),
                safeTrimOrNull(avamJobAdvertisement.getStellennummerAvam()),
                parseToLocalDate(avamJobAdvertisement.getAblehnungDatum()),
                avamJobAdvertisement.getAblehnungGrundCode(),
                avamJobAdvertisement.getAblehnungGrund(),
                avamJobAdvertisement.getArbeitsamtBereich()
        );
    }

    CancellationDto createCancellationDto(WSOsteEgov avamJobAdvertisement) {
        return new CancellationDto(
                safeTrimOrNull(avamJobAdvertisement.getStellennummerEgov()),
                safeTrimOrNull(avamJobAdvertisement.getStellennummerAvam()),
                parseToLocalDate(avamJobAdvertisement.getAbmeldeDatum()),
                CANCELLATION_CODE.getRight(avamJobAdvertisement.getAbmeldeGrundCode())
        );
    }

    private ContactDto createContactDto(WSOsteEgov avamJobAdvertisement) {
        if (hasText(avamJobAdvertisement.getKpAnredeCode()) ||
                hasText(avamJobAdvertisement.getKpVorname()) ||
                hasText(avamJobAdvertisement.getKpName()) ||
                hasText(avamJobAdvertisement.getKpTelefonNr()) ||
                hasText(avamJobAdvertisement.getKpEmail())) {
            return new ContactDto(
                    hasText(avamJobAdvertisement.getKpAnredeCode()) ? SALUTATIONS.getRight(avamJobAdvertisement.getKpAnredeCode()) : Salutation.MR,
                    avamJobAdvertisement.getKpVorname(),
                    avamJobAdvertisement.getKpName(),
                    sanitizePhoneNumber(avamJobAdvertisement.getKpTelefonNr(), avamJobAdvertisement),
                    sanitizeEmail(avamJobAdvertisement.getKpEmail(), avamJobAdvertisement),
                    "" // Not defined in this AVAM version
            );
        }
        return null;
    }

    private EmploymentDto createEmploymentDto(WSOsteEgov avamJobAdvertisement) {
        WorkingTimePercentage workingTimePercentage = WorkingTimePercentage.evaluate(avamJobAdvertisement.getPensumVon(), avamJobAdvertisement.getPensumBis());
        return new EmploymentDto(
                parseToLocalDate(avamJobAdvertisement.getStellenantritt()),
                parseToLocalDate(avamJobAdvertisement.getVertragsdauer()),
                safeBoolean(avamJobAdvertisement.isKurzeinsatz(), false),
                safeBoolean(avamJobAdvertisement.isAbSofort(), avamJobAdvertisement.getStellenantritt() == null),
                safeBoolean(avamJobAdvertisement.isUnbefristet(), avamJobAdvertisement.getVertragsdauer() == null),
                workingTimePercentage.getMin(),
                workingTimePercentage.getMax(),
                createWorkForms(avamJobAdvertisement)
        );
    }

    private CreateLocationDto createCreateLocationDto(WSOsteEgov avamJobAdvertisement) {
        return new CreateLocationDto(
                avamJobAdvertisement.getArbeitsOrtText(),
                avamJobAdvertisement.getArbeitsOrtOrt(),
                avamJobAdvertisement.getArbeitsOrtPlz(),
                avamJobAdvertisement.getArbeitsOrtLand()
        );
    }

    private CompanyDto createCompanyDto(WSOsteEgov avamJobAdvertisement) {
        // This fields are also used for ApplChannel from AVAM
        return new CompanyDto(
                avamJobAdvertisement.getUntName(),
                avamJobAdvertisement.getUntStrasse(),
                avamJobAdvertisement.getUntHausNr(),
                avamJobAdvertisement.getUntPlz(),
                avamJobAdvertisement.getUntOrt(),
                avamJobAdvertisement.getUntLand(),
                avamJobAdvertisement.getUntPostfach(),
                avamJobAdvertisement.getUntPostfachPlz(),
                avamJobAdvertisement.getUntPostfachOrt(),
                null, // This is only used for ApplyChannel from AVAM
                null, // This is only used for ApplyChannel from AVAM
                null, // This is only used for ApplyChannel from AVAM
                false
        );
    }

    private ApplyChannelDto createApplyChannelDto(WSOsteEgov avamJobAdvertisement) {
        return new ApplyChannelDto(
                avamJobAdvertisement.isBewerSchriftlich() ? createApplyMailAddress(avamJobAdvertisement) : null,
                avamJobAdvertisement.isBewerElektronisch() ? sanitizeEmail(avamJobAdvertisement.getUntEmail(), avamJobAdvertisement) : null,
                avamJobAdvertisement.isBewerTelefonisch() ? sanitizePhoneNumber(avamJobAdvertisement.getUntTelefon(), avamJobAdvertisement) : null,
                avamJobAdvertisement.isBewerElektronisch() ? sanitizeUrl(avamJobAdvertisement.getUntUrl(), avamJobAdvertisement) : null,
                avamJobAdvertisement.getBewerAngaben()
        );
    }

    private List<OccupationDto> createOccupationDtos(WSOsteEgov avamJobAdvertisement) {
        return Stream
                .of(
                        createOccupationDto(avamJobAdvertisement.getBq1AvamBerufNr(), avamJobAdvertisement.getBq1ErfahrungCode(), avamJobAdvertisement.getBq1AusbildungCode()),
                        createOccupationDto(avamJobAdvertisement.getBq2AvamBerufNr(), avamJobAdvertisement.getBq2ErfahrungCode(), avamJobAdvertisement.getBq2AusbildungCode()),
                        createOccupationDto(avamJobAdvertisement.getBq3AvamBerufNr(), avamJobAdvertisement.getBq3ErfahrungCode(), avamJobAdvertisement.getBq3AusbildungCode())
                )
                .filter(Objects::nonNull)
                .collect(toList());
    }

    private OccupationDto createOccupationDto(BigInteger avamBerufNr, String erfahrungCode, String ausbildungCode) {
        if (nonNull(avamBerufNr)) {
            return new OccupationDto(
                    avamBerufNr.toString(),
                    EXPERIENCES.getRight(erfahrungCode),
                    ausbildungCode
            );
        }
        return null;
    }

    private List<LanguageSkillDto> createLanguageSkillDtos(WSOsteEgov avamJobAdvertisement) {
        return Stream
                .of(
                        createLanguageSkillDto(avamJobAdvertisement.getSk1SpracheCode(), avamJobAdvertisement.getSk1MuendlichCode(), avamJobAdvertisement.getSk1SchriftlichCode()),
                        createLanguageSkillDto(avamJobAdvertisement.getSk2SpracheCode(), avamJobAdvertisement.getSk2MuendlichCode(), avamJobAdvertisement.getSk2SchriftlichCode()),
                        createLanguageSkillDto(avamJobAdvertisement.getSk3SpracheCode(), avamJobAdvertisement.getSk3MuendlichCode(), avamJobAdvertisement.getSk3SchriftlichCode()),
                        createLanguageSkillDto(avamJobAdvertisement.getSk4SpracheCode(), avamJobAdvertisement.getSk4MuendlichCode(), avamJobAdvertisement.getSk4SchriftlichCode()),
                        createLanguageSkillDto(avamJobAdvertisement.getSk5SpracheCode(), avamJobAdvertisement.getSk5MuendlichCode(), avamJobAdvertisement.getSk5SchriftlichCode())
                )
                .filter(Objects::nonNull)
                .collect(toList());
    }

    private LanguageSkillDto createLanguageSkillDto(String spracheCode, String muendlichCode, String schriftlichCode) {
        if (hasText(spracheCode)) {
            return new LanguageSkillDto(
                    LANGUAGES.getRight(spracheCode),
                    LANGUAGE_LEVEL.getRight(muendlichCode),
                    LANGUAGE_LEVEL.getRight(schriftlichCode)
            );
        }
        return null;
    }

    private PublicationDto createPublicationDto(WSOsteEgov avamJobAdvertisement) {
        return new PublicationDto(
                parseToLocalDate(avamJobAdvertisement.getAnmeldeDatum()),
                parseToLocalDate(avamJobAdvertisement.getGueltigkeit()),
                avamJobAdvertisement.isEures(),
                avamJobAdvertisement.isEuresAnonym(),
                avamJobAdvertisement.isPublikation(),
                avamJobAdvertisement.isLoginPublikation(),
                avamJobAdvertisement.isAnonym() || avamJobAdvertisement.isLoginAnonym()
        );
    }

    private Set<WorkForm> createWorkForms(WSOsteEgov avamJobAdvertisement) {
        WSArbeitsformArray arbeitsformCodeList = avamJobAdvertisement.getArbeitsformCodeList();
        if (arbeitsformCodeList != null) {
            return arbeitsformCodeList.getWSArbeitsformArrayItem()
                    .stream()
                    .map(item -> WORK_FORMS.getRight(item.getArbeitsformCode()))
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    /*
     * Check for a valid phone number and remove remarks.
     */
    private String sanitizePhoneNumber(String phone, WSOsteEgov avamJobAdvertisement) {
        if (hasText(phone)) {
            try {
                Phonenumber.PhoneNumber phoneNumber = PhoneNumberUtil.getInstance().parse(phone, "CH");
                if (PhoneNumberUtil.getInstance().isValidNumber(phoneNumber)) {
                    return PhoneNumberUtil.getInstance().format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
                }
            } catch (NumberParseException e) {
                LOG.warn("JobAd stellennummerAvam: {} has invalid phone number: {}", avamJobAdvertisement.getStellennummerAvam(), phone);
                String[] phoneParts = phone.split("[^\\d\\(\\)\\+ ]");
                if (phoneParts.length > 1) {
                    return sanitizePhoneNumber(phoneParts[0], avamJobAdvertisement);
                }
            }
        }
        return null;
    }

    private String sanitizeEmail(String testObject, WSOsteEgov avamJobAdvertisement) {
        if (hasText(testObject)) {
            String email = trimAllWhitespace(testObject).replace("'", "");
            if (emailValidator.isValid(email, null)) {
                return email;
            } else {
                LOG.warn("JobAd stellennummerAvam: {} has invalid email: {}", avamJobAdvertisement.getStellennummerAvam(), testObject);
            }
        }
        return null;
    }

    private String sanitizeUrl(String testObject, WSOsteEgov avamJobAdvertisement) {
        if (hasText(testObject)) {
            try {
                URL url = new URL(testObject);
                return url.toExternalForm();
            } catch (MalformedURLException e) {
                LOG.warn("JobAd stellennummerAvam: {} has invalid URL: {}", avamJobAdvertisement.getStellennummerAvam(), testObject);
                try {
                    URL url = new URL("http://" + testObject);
                    return url.toExternalForm();
                } catch (MalformedURLException e1) {
                }
            }
        }
        return null;
    }

    private String createApplyMailAddress(WSOsteEgov avamJobAdvertisement) {
        StringBuilder sb = new StringBuilder();
        sb.append(avamJobAdvertisement.getUntName());
        if (hasText(avamJobAdvertisement.getUntStrasse())) {
            sb.append(", ");
            sb.append(avamJobAdvertisement.getUntStrasse());
            sb.append(' ');
            sb.append(avamJobAdvertisement.getUntHausNr());
        }
        if (hasText(avamJobAdvertisement.getUntPostfach())) {
            sb.append(", ");
            sb.append(avamJobAdvertisement.getUntPostfach());
            sb.append(", ");
            sb.append(avamJobAdvertisement.getUntPostfachPlz());
            sb.append(' ');
            sb.append(avamJobAdvertisement.getUntPostfachOrt());
        } else if (hasText(avamJobAdvertisement.getUntPlz())) {
            sb.append(", ");
            sb.append(avamJobAdvertisement.getUntPlz());
            sb.append(' ');
            sb.append(avamJobAdvertisement.getUntOrt());
        }
        return sb.toString();
    }

}
