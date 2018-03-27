package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import static ch.admin.seco.jobs.services.jobadservice.domain.avam.AvamCodeResolver.EXPERIENCES;
import static ch.admin.seco.jobs.services.jobadservice.domain.avam.AvamCodeResolver.LANGUAGES;
import static ch.admin.seco.jobs.services.jobadservice.domain.avam.AvamCodeResolver.LANGUAGE_LEVEL;
import static ch.admin.seco.jobs.services.jobadservice.domain.avam.AvamCodeResolver.SALUTATIONS;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.AvamDateTimeFormatter.parseToLocalDate;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.StringUtils.hasText;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.util.StringUtils;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.ApplyChannelDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.ApprovalDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.CancellationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.CompanyDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.ContactDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.CreateJobAdvertisementAvamDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.CreateLocationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.EmploymentDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.LanguageSkillDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.OccupationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.PublicationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.RejectionDto;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.WorkForm;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.source.WSOsteEgov;

public class JobAdvertisementFromAvamAssembler {

    private static final Logger LOG = LoggerFactory.getLogger(JobAdvertisementFromAvamAssembler.class);
    private static final EmailValidator emailValidator = new EmailValidator();

    ApprovalDto createApprovaldDto(WSOsteEgov avamJobAdvertisement) {
        return new ApprovalDto(
                null,
                avamJobAdvertisement.getStellennummerEgov(),
                avamJobAdvertisement.getStellennummerAvam(),
                parseToLocalDate(avamJobAdvertisement.getAnmeldeDatum()),
                true, // TODO set value from AVAM
                parseToLocalDate(null)); // TODO set value from AVAM
    }

    RejectionDto createRejectionDto(WSOsteEgov avamJobAdvertisement) {
        return new RejectionDto(
                null,
                avamJobAdvertisement.getStellennummerEgov(),
                avamJobAdvertisement.getStellennummerAvam(),
                LocalDate.now(),
                avamJobAdvertisement.getAbmeldeGrundCode(),
                avamJobAdvertisement.getDetailangabenCode()
        );
    }

    CreateJobAdvertisementAvamDto createCreateJobAdvertisementAvamDto(WSOsteEgov avamJobAdvertisement) {
        return new CreateJobAdvertisementAvamDto(
                avamJobAdvertisement.getStellennummerAvam(),
                avamJobAdvertisement.getBezeichnung(),
                avamJobAdvertisement.getBeschreibung(),
                "de", //TODO get correct value from Avam
                avamJobAdvertisement.getArbeitsamtBereich(),
                createEmploymentDto(avamJobAdvertisement),
                createApplyChannelDto(avamJobAdvertisement),
                createCompanyDto(avamJobAdvertisement),
                createContactDto(avamJobAdvertisement),
                createCreateLocationDto(avamJobAdvertisement),
                createOccupationDtos(avamJobAdvertisement),
                createLanguageDtos(avamJobAdvertisement),
                createPublicationDto(avamJobAdvertisement),
                createWorkForms(avamJobAdvertisement)
        );
    }

    CancellationDto createCancellationDto(WSOsteEgov avamJobAdvertisement) {
        return new CancellationDto(
                null,
                avamJobAdvertisement.getStellennummerEgov(),
                avamJobAdvertisement.getStellennummerAvam(),
                parseToLocalDate(avamJobAdvertisement.getAbmeldeDatum()),
                avamJobAdvertisement.getAbmeldeGrundCode());
    }

    private ContactDto createContactDto(WSOsteEgov avamJobAdvertisement) {
        return new ContactDto(
                SALUTATIONS.getRight(avamJobAdvertisement.getKpAnredeCode()),
                avamJobAdvertisement.getKpVorname(),
                avamJobAdvertisement.getKpName(),
                sanitizePhoneNumber(avamJobAdvertisement.getKpTelefonNr(), avamJobAdvertisement),
                sanitizeEmail(avamJobAdvertisement.getKpEmail(), avamJobAdvertisement),
                "de"); // TODO how to match this value from avam?
    }

    private EmploymentDto createEmploymentDto(WSOsteEgov avamJobAdvertisement) {
        return new EmploymentDto(
                parseToLocalDate(avamJobAdvertisement.getAnmeldeDatum()),
                parseToLocalDate(avamJobAdvertisement.getGueltigkeit()),
                null,
                avamJobAdvertisement.isAbSofort(),
                avamJobAdvertisement.isUnbefristet(),
                nonNull(avamJobAdvertisement.getPensumVon()) ? avamJobAdvertisement.getPensumVon().intValue() : 100,
                nonNull(avamJobAdvertisement.getPensumBis()) ? avamJobAdvertisement.getPensumBis().intValue() : 100
        );
    }

    private CreateLocationDto createCreateLocationDto(WSOsteEgov avamJobAdvertisement) {
        return new CreateLocationDto(
                avamJobAdvertisement.getArbeitsOrtText(),
                avamJobAdvertisement.getArbeitsOrtOrt(),
                avamJobAdvertisement.getArbeitsOrtPlz(),
                avamJobAdvertisement.getArbeitsOrtLand(),
                avamJobAdvertisement.getArbeitsOrtGemeinde());
    }

    private CompanyDto createCompanyDto(WSOsteEgov avamJobAdvertisement) {
        return new CompanyDto(avamJobAdvertisement.getUntName(),
                avamJobAdvertisement.getUntStrasse(),
                avamJobAdvertisement.getUntHausNr(),
                avamJobAdvertisement.getUntPlz(),
                avamJobAdvertisement.getUntOrt(),
                avamJobAdvertisement.getUntLand(),
                avamJobAdvertisement.getUntPostfach(),
                avamJobAdvertisement.getUntPostfachPlz(),
                avamJobAdvertisement.getUntPostfachOrt(),
                null, null, null);
    }

    private ApplyChannelDto createApplyChannelDto(WSOsteEgov avamJobAdvertisement) {
        return new ApplyChannelDto(
                String.valueOf(safeBoolean(avamJobAdvertisement.isBewerSchriftlich())), // TODO what is the expected value?
                sanitizeEmail(avamJobAdvertisement.getUntEmail(), avamJobAdvertisement),
                sanitizePhoneNumber(avamJobAdvertisement.getUntTelefon(), avamJobAdvertisement),
                sanitizeUrl(avamJobAdvertisement.getUntUrl(), avamJobAdvertisement),
                avamJobAdvertisement.getBewerAngaben());
    }

    private List<OccupationDto> createOccupationDtos(WSOsteEgov avamJobAdvertisement) {
        return Stream.of(
                createOccupationDto(avamJobAdvertisement.getBq1AvamBerufNr(), avamJobAdvertisement.getBq1ErfahrungCode(), avamJobAdvertisement.getBq1AusbildungCode()),
                createOccupationDto(avamJobAdvertisement.getBq2AvamBerufNr(), avamJobAdvertisement.getBq2ErfahrungCode(), avamJobAdvertisement.getBq2AusbildungCode()),
                createOccupationDto(avamJobAdvertisement.getBq2AvamBerufNr(), avamJobAdvertisement.getBq3ErfahrungCode(), avamJobAdvertisement.getBq2AusbildungCode()))
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

    private List<LanguageSkillDto> createLanguageDtos(WSOsteEgov avamJobAdvertisement) {
        return Stream.of(
                createLanguageSkill(avamJobAdvertisement.getSk1SpracheCode(), avamJobAdvertisement.getSk1MuendlichCode(), avamJobAdvertisement.getSk1SchriftlichCode()),
                createLanguageSkill(avamJobAdvertisement.getSk2SpracheCode(), avamJobAdvertisement.getSk2MuendlichCode(), avamJobAdvertisement.getSk2SchriftlichCode()),
                createLanguageSkill(avamJobAdvertisement.getSk3SpracheCode(), avamJobAdvertisement.getSk3MuendlichCode(), avamJobAdvertisement.getSk3SchriftlichCode()),
                createLanguageSkill(avamJobAdvertisement.getSk4SpracheCode(), avamJobAdvertisement.getSk4MuendlichCode(), avamJobAdvertisement.getSk4SchriftlichCode()),
                createLanguageSkill(avamJobAdvertisement.getSk5SpracheCode(), avamJobAdvertisement.getSk5MuendlichCode(), avamJobAdvertisement.getSk5SchriftlichCode()))
                .filter(Objects::nonNull)
                .collect(toList());
    }

    private LanguageSkillDto createLanguageSkill(String sprachCode, String muendlichCode, String schriftlichCode) {
        if (hasText(sprachCode)) {
            return new LanguageSkillDto(
                    LANGUAGES.getRight(sprachCode),
                    LANGUAGE_LEVEL.getRight(muendlichCode),
                    LANGUAGE_LEVEL.getRight(schriftlichCode)
            );
        }
        return null;
    }

    private PublicationDto createPublicationDto(WSOsteEgov avamJobAdvertisement) {
        return new PublicationDto(avamJobAdvertisement.isEures(),
                avamJobAdvertisement.isEuresAnonym(),
                avamJobAdvertisement.isAnonym(),
                avamJobAdvertisement.isPublikation(),
                false, //TODO: Set from AVAM
                false //TODO: Set from AVAM
        );
    }

    private Set<WorkForm> createWorkForms(WSOsteEgov avamJobAdvertisement) {
        //TODO: Set from AVAM
        return Collections.emptySet();
    }

    private boolean safeBoolean(Boolean value) {
        return (value != null) ? value : false;
    }

    /*
     * Check for a valid phone number and remove remarks.
     */
    private String sanitizePhoneNumber(String phone, WSOsteEgov avamJobAdvertisement) {
        if (StringUtils.hasText(phone)) {
            try {
                Phonenumber.PhoneNumber phoneNumber = PhoneNumberUtil.getInstance().parse(phone, "CH");
                if (PhoneNumberUtil.getInstance().isValidNumber(phoneNumber)) {
                    return PhoneNumberUtil.getInstance().format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
                }
            } catch (NumberParseException e) {
                LOG.warn("JobAd stellennummerAvam: {} has invalid phone number: {}", avamJobAdvertisement.getStellennummerAvam(), phone);
                String[] phoneParts = phone.toString().split("[^\\d\\(\\)\\+ ]");
                if (phoneParts.length > 1) {
                    return sanitizePhoneNumber(phoneParts[0], avamJobAdvertisement);
                }
            }
        }
        return null;
    }

    private String sanitizeEmail(String testObject, WSOsteEgov avamJobAdvertisement) {
        if (StringUtils.hasText(testObject)) {
            String email = StringUtils.trimAllWhitespace(testObject).replace("'", "");
            if (emailValidator.isValid(email, null)) {
                return email;
            } else {
                LOG.warn("JobAd stellennummerAvam: {} has invalid email: {}", avamJobAdvertisement.getStellennummerAvam(), testObject);
            }
        }
        return null;
    }

    private String sanitizeUrl(String testObject, WSOsteEgov avamJobAdvertisement) {
        if (StringUtils.hasText(testObject)) {
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
}
