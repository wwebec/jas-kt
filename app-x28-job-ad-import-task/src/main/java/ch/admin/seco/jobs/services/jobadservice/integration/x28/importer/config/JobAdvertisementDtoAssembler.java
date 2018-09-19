package ch.admin.seco.jobs.services.jobadservice.integration.x28.importer.config;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.EmploymentDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.*;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageLevel;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Salutation;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.WorkExperience;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils.WorkingTimePercentage;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.AvamCodeResolver;
import ch.admin.seco.jobs.services.jobadservice.integration.x28.jobadimport.Oste;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.apache.commons.lang3.EnumUtils;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.AvamCodeResolver.LANGUAGES;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.AvamCodeResolver.LANGUAGE_LEVEL;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.StringUtils.*;

class JobAdvertisementDtoAssembler {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobAdvertisementDtoAssembler.class);

    private static final Pattern COUNTRY_ZIPCODE_CITY_PATTERN = Pattern.compile("\\b(([A-Z]{2,3})?[ -]+)?(\\d{4,5})? ?([\\wäöüéèàâôÄÖÜÉÈÀ /.-]+)");
    private static final Pattern CITY_CANTON_PATTERN = Pattern.compile("(.* |^)\\(?([A-Z]{2})\\)?( \\d)?$");
    private static final String LICHTENSTEIN_ISO_CODE = "LI";
    private static final String ORACLE_DATE_FORMAT = "yyyy-MM-dd-HH.mm.ss.SSSSSS";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(ORACLE_DATE_FORMAT);
    private static final EmailValidator EMAIL_VALIDATOR = new EmailValidator();
    private static final String DEFAULT_AVAM_OCCUPATION_CODE = "99999";
    private static final String COUNTRY_ISO_CODE_SWITZERLAND = "CH";

    CreateJobAdvertisementFromX28Dto createJobAdvertisementFromX28Dto(Oste x28JobAdvertisement) {
        LocalDate publicationStartDate = parseDate(x28JobAdvertisement.getAnmeldeDatum());
        LocalDate publicationEndDate = parseDate(x28JobAdvertisement.getGueltigkeit());

        return new CreateJobAdvertisementFromX28Dto()
                .setStellennummerEgov(x28JobAdvertisement.getStellennummerEGov())
                .setStellennummerAvam(x28JobAdvertisement.getStellennummerAvam())
                .setTitle(sanitize(x28JobAdvertisement.getBezeichnung()))
                .setDescription(sanitize(x28JobAdvertisement.getBeschreibung()))
                .setNumberOfJobs(x28JobAdvertisement.getGleicheOste())
                .setFingerprint(x28JobAdvertisement.getFingerprint())
                .setExternalUrl(x28JobAdvertisement.getUrl())
                .setJobCenterCode(x28JobAdvertisement.getArbeitsamtbereich())
                .setContact(createContact(x28JobAdvertisement))
                .setEmployment(createEmployment(x28JobAdvertisement))
                .setCompany(createCompany(x28JobAdvertisement))
                .setLocation(createLocation(x28JobAdvertisement))
                .setOccupations(createOccupations(x28JobAdvertisement))
                .setProfessionCodes(createProfessionCodes(x28JobAdvertisement))
                .setLanguageSkills(createLanguageSkills(x28JobAdvertisement))
                .setPublicationStartDate(determineStartDate(publicationStartDate, publicationEndDate))
                .setPublicationEndDate(determineEndDate(publicationStartDate, publicationEndDate))
                .setCompanyAnonymous(fallbackAwareBoolean(x28JobAdvertisement.isWwwAnonym(), false));

    }

    private LocalDate determineStartDate(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            return null;
        }
        if (endDate == null) {
            return startDate;
        }
        if (startDate.isAfter(endDate)) {
            return endDate;
        }
        return startDate;
    }

    private LocalDate determineEndDate(LocalDate startDate, LocalDate endDate) {
        if (endDate == null) {
            return null;
        }
        if (startDate == null) {
            return endDate;
        }
        if (endDate.isBefore(startDate)) {
            return startDate;
        }
        return endDate;
    }

    private List<X28LanguageSkillDto> createLanguageSkills(Oste x28JobAdvertisement) {
        return Stream
                .of(
                        createLanguageSkillDto(x28JobAdvertisement.getSk1SpracheCode(), x28JobAdvertisement.getSk1MuendlichCode(), x28JobAdvertisement.getSk1SchriftlichCode()),
                        createLanguageSkillDto(x28JobAdvertisement.getSk2SpracheCode(), x28JobAdvertisement.getSk2MuendlichCode(), x28JobAdvertisement.getSk2SchriftlichCode()),
                        createLanguageSkillDto(x28JobAdvertisement.getSk3SpracheCode(), x28JobAdvertisement.getSk3MuendlichCode(), x28JobAdvertisement.getSk3SchriftlichCode()),
                        createLanguageSkillDto(x28JobAdvertisement.getSk4SpracheCode(), x28JobAdvertisement.getSk4MuendlichCode(), x28JobAdvertisement.getSk4SchriftlichCode()),
                        createLanguageSkillDto(x28JobAdvertisement.getSk5SpracheCode(), x28JobAdvertisement.getSk5MuendlichCode(), x28JobAdvertisement.getSk5SchriftlichCode())
                )
                .filter(Objects::nonNull)
                .collect(toList());
    }

    private X28LanguageSkillDto createLanguageSkillDto(String spracheCode, String muendlichCode, String schriftlichCode) {
        if (hasText(spracheCode)) {
            final String languageIsoCode;
            final LanguageLevel spokenLevel;
            final LanguageLevel writtenLevel;

            if (LANGUAGES.getRight(spracheCode) != null) {
                languageIsoCode = LANGUAGES.getRight(spracheCode);
                spokenLevel = LANGUAGE_LEVEL.getRight(muendlichCode);
                writtenLevel = LANGUAGE_LEVEL.getRight(schriftlichCode);

            } else if (LANGUAGES.getLeft(spracheCode) != null) {
                languageIsoCode = spracheCode;
                spokenLevel = EnumUtils.getEnum(LanguageLevel.class, muendlichCode);
                writtenLevel = EnumUtils.getEnum(LanguageLevel.class, schriftlichCode);

            } else {
                languageIsoCode = null;
                spokenLevel = null;
                writtenLevel = null;
            }

            if (hasText(languageIsoCode)) {
                return new X28LanguageSkillDto(languageIsoCode, spokenLevel, writtenLevel);
            } else {
                LOGGER.warn("No languageIsoCode was found for: {} ", spracheCode);
            }
        }
        return null;
    }

    private String createProfessionCodes(Oste x28JobAdvertisement) {
        List<Integer> berufsBezeichnungen = x28JobAdvertisement.getBerufsBezeichnungen();
        if ((berufsBezeichnungen != null) && !berufsBezeichnungen.isEmpty()) {
            return berufsBezeichnungen.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
        }
        return null;
    }

    private List<X28OccupationDto> createOccupations(Oste x28JobAdvertisement) {
        List<X28OccupationDto> occupations = new ArrayList<>();
        if (hasText(x28JobAdvertisement.getBq1AvamBerufNr())) {
            occupations.add(new X28OccupationDto(
                    fallbackAwareAvamOccuptionCode(x28JobAdvertisement.getBq1AvamBerufNr()),
                    resolveExperience(x28JobAdvertisement.getBq1ErfahrungCode()),
                    x28JobAdvertisement.getBq1AusbildungCode()
            ));
        }
        if (hasText(x28JobAdvertisement.getBq2AvamBerufNr())) {
            occupations.add(new X28OccupationDto(
                    fallbackAwareAvamOccuptionCode(x28JobAdvertisement.getBq2AvamBerufNr()),
                    resolveExperience(x28JobAdvertisement.getBq2ErfahrungCode()),
                    x28JobAdvertisement.getBq2AusbildungCode()
            ));
        }
        if (hasText(x28JobAdvertisement.getBq3AvamBerufNr())) {
            occupations.add(new X28OccupationDto(
                    fallbackAwareAvamOccuptionCode(x28JobAdvertisement.getBq3AvamBerufNr()),
                    resolveExperience(x28JobAdvertisement.getBq3ErfahrungCode()),
                    x28JobAdvertisement.getBq3AusbildungCode()
            ));
        }

        if (occupations.isEmpty()) {
            occupations.add(new X28OccupationDto(DEFAULT_AVAM_OCCUPATION_CODE, null, null));
        }

        return occupations;
    }

    private X28LocationDto createLocation(Oste x28JobAdvertisement) {
        X28LocationDto createLocationDto = extractLocation(x28JobAdvertisement.getArbeitsortText());

        if (createLocationDto == null) {
            return null;
        }

        if (x28JobAdvertisement.getArbeitsortPlz() != null) {
            createLocationDto.setPostalCode(x28JobAdvertisement.getArbeitsortPlz());
        }

        if (createLocationDto.getCountryIsoCode() == null) {
            if (LICHTENSTEIN_ISO_CODE.equals(x28JobAdvertisement.getArbeitsortKanton())) {
                createLocationDto.setCountryIsoCode(LICHTENSTEIN_ISO_CODE);
            } else if (x28JobAdvertisement.getArbeitsortAusland() != null) {
                createLocationDto.setCountryIsoCode(x28JobAdvertisement.getArbeitsortAusland());
            } else {
                createLocationDto.setCountryIsoCode(COUNTRY_ISO_CODE_SWITZERLAND);
            }
        }

        return createLocationDto;
    }

    private X28CompanyDto createCompany(Oste x28JobAdvertisement) {
        return new X28CompanyDto(
                x28JobAdvertisement.getUntName(),
                x28JobAdvertisement.getUntStrasse(),
                x28JobAdvertisement.getUntHausNr(),
                x28JobAdvertisement.getUntPlz(),
                x28JobAdvertisement.getUntOrt(),
                x28JobAdvertisement.getUntLand(),
                x28JobAdvertisement.getUntPostfach(),
                x28JobAdvertisement.getUntPostfachPlz(),
                x28JobAdvertisement.getUntPostfachOrt(),
                x28JobAdvertisement.getUntTelefon(),
                x28JobAdvertisement.getUntEMail(),
                x28JobAdvertisement.getUntUrl(),
                false
        );
    }

    private X28ContactDto createContact(Oste x28JobAdvertisement) {
        if (hasText(x28JobAdvertisement.getKpAnredeCode()) ||
                hasText(x28JobAdvertisement.getKpVorname()) ||
                hasText(x28JobAdvertisement.getKpName()) ||
                hasText(x28JobAdvertisement.getKpTelefonNr()) ||
                hasText(x28JobAdvertisement.getKpEMail())) {
            return new X28ContactDto(
                    resolveSalutation(x28JobAdvertisement.getKpAnredeCode()),
                    x28JobAdvertisement.getKpVorname(),
                    x28JobAdvertisement.getKpName(),
                    sanitizePhoneNumber(x28JobAdvertisement.getKpTelefonNr(), x28JobAdvertisement),
                    sanitizeEmail(x28JobAdvertisement.getKpEMail(), x28JobAdvertisement),
                    "de" // Not defined in this AVAM version
            );
        }
        return null;
    }

    private EmploymentDto createEmployment(Oste x28JobAdvertisement) {
        LocalDate startDate = parseDate(x28JobAdvertisement.getStellenantritt());
        LocalDate endDate = parseDate(x28JobAdvertisement.getVertragsdauer());
        WorkingTimePercentage workingTimePercentage = WorkingTimePercentage.evaluate(x28JobAdvertisement.getPensumVon(), x28JobAdvertisement.getPensumBis());
        return new EmploymentDto(
                startDate,
                endDate,
                false,
                fallbackAwareBoolean(x28JobAdvertisement.isAbSofort(), false),
                fallbackAwareBoolean(x28JobAdvertisement.isUnbefristet(), (endDate != null)),
                workingTimePercentage.getMin(),
                workingTimePercentage.getMax(),
                null
        );
    }

    private Salutation resolveSalutation(String kpAnredeCode) {
        final Salutation resolvedSalutation;

        if (hasText(kpAnredeCode)) {
            if (AvamCodeResolver.SALUTATIONS.getRight(kpAnredeCode) != null) {
                resolvedSalutation = AvamCodeResolver.SALUTATIONS.getRight(kpAnredeCode);
            } else {
                resolvedSalutation = EnumUtils.getEnum(Salutation.class, kpAnredeCode);
            }
        } else {
            resolvedSalutation = null;
        }

        return resolvedSalutation != null ? resolvedSalutation : Salutation.MR;
    }

    private WorkExperience resolveExperience(String experienceCode) {
        final WorkExperience resolvedWorkExperience;

        if (AvamCodeResolver.EXPERIENCES.getRight(experienceCode) != null) {
            resolvedWorkExperience = AvamCodeResolver.EXPERIENCES.getRight(experienceCode);
        } else {
            resolvedWorkExperience = EnumUtils.getEnum(WorkExperience.class, experienceCode);
        }

        return resolvedWorkExperience;
    }

    private String sanitize(String text) {
        if (hasText(text)) {
            // remove javascript injection and css styles
            String sanitizedText = Jsoup.clean(text, "", Whitelist.basic(), new Document.OutputSettings().prettyPrint(false));

            // replace exotic bullet points with proper dash character
            return sanitizedText.replaceAll("[^\\p{InBasic_Latin}\\p{InLatin-1Supplement}]", "-");
        }
        return text;
    }

    /*
     * Check for a valid phone number and remove remarks.
     */
    private String sanitizePhoneNumber(String phone, Oste x28JobAdvertisement) {
        if (hasText(phone)) {
            try {
                Phonenumber.PhoneNumber phoneNumber = PhoneNumberUtil.getInstance().parse(phone, "CH");
                if (PhoneNumberUtil.getInstance().isValidNumber(phoneNumber)) {
                    return PhoneNumberUtil.getInstance().format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
                }
            } catch (NumberParseException e) {
                LOGGER.warn("JobAd fingerprint: {} has invalid phone number: {}", x28JobAdvertisement.getFingerprint(), phone);
                String[] phoneParts = phone.split("[^\\d\\(\\)\\+ ]");
                if (phoneParts.length > 1) {
                    return sanitizePhoneNumber(phoneParts[0], x28JobAdvertisement);
                }
            }
        }
        return null;
    }

    private String sanitizeEmail(String testObject, Oste x28JobAdvertisement) {
        if (hasText(testObject)) {
            String email = trimAllWhitespace(testObject).replace("'", "");
            if (EMAIL_VALIDATOR.isValid(email, null)) {
                return email;
            } else {
                LOGGER.warn("JobAd fingerprint: {} has invalid email: {}", x28JobAdvertisement.getFingerprint(), testObject);
            }
        }
        return null;
    }

    private X28LocationDto extractLocation(String localityText) {
        if (hasText(localityText)) {
            Matcher countryZipCodeCityMatcher = COUNTRY_ZIPCODE_CITY_PATTERN.matcher(localityText);
            if (countryZipCodeCityMatcher.find()) {
                String countryIsoCode = countryZipCodeCityMatcher.group(2);
                if (hasText(countryIsoCode)) {
                    countryIsoCode = countryIsoCode.substring(0, 2);
                    if ("FL".equals(countryIsoCode)) {
                        countryIsoCode = LICHTENSTEIN_ISO_CODE;
                    }
                }
                String postalCode = StringUtils.trimWhitespace(countryZipCodeCityMatcher.group(3));
                String city = StringUtils.trimWhitespace(countryZipCodeCityMatcher.group(4));
                if (hasText(city)) {
                    Matcher cityCantonMatcher = CITY_CANTON_PATTERN.matcher(city);
                    if (cityCantonMatcher.find()) {
                        city = StringUtils.trimWhitespace(cityCantonMatcher.group(1));
                    }
                }
                return new X28LocationDto(null, city, postalCode, countryIsoCode);
            }
        }
        return null;
    }

    private LocalDate parseDate(String startDate) {
        if (isEmpty(startDate)) {
            return null;
        }
        return LocalDate.parse(startDate, DATE_FORMATTER);
    }

    private boolean fallbackAwareBoolean(Boolean value, boolean defaultValue) {
        return (value != null) ? value : defaultValue;
    }

    private String fallbackAwareAvamOccuptionCode(String avamOccupationCode) {
        return hasText(avamOccupationCode) ? avamOccupationCode : DEFAULT_AVAM_OCCUPATION_CODE;
    }

}
