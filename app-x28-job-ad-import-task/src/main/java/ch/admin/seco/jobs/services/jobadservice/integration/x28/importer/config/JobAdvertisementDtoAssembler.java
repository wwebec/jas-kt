package ch.admin.seco.jobs.services.jobadservice.integration.x28.importer.config;

import static org.springframework.util.StringUtils.hasText;
import static org.springframework.util.StringUtils.isEmpty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import org.springframework.util.StringUtils;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.CompanyDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.EmploymentDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.OccupationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateLocationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.UpdateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils.WorkingTimePercentage;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.AvamCodeResolver;
import ch.admin.seco.jobs.services.jobadservice.integration.x28.jobadimport.Oste;

class JobAdvertisementDtoAssembler {

    private static final Pattern COUNTRY_ZIPCODE_CITY_PATTERN = Pattern.compile("\\b(([A-Z]{2,3})?[ -]+)?(\\d{4,5})? ?([\\wäöüéèàâôÄÖÜÉÈÀ /.-]+)");
    private static final Pattern CITY_CANTON_PATTERN = Pattern.compile("(.* |^)\\(?([A-Z]{2})\\)?( \\d)?$");
    private static final String LICHTENSTEIN_ISO_CODE = "LI";
    private static final String ORACLE_DATE_FORMAT = "yyyy-MM-dd-HH.mm.ss.SSSSSS";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(ORACLE_DATE_FORMAT);

    CreateJobAdvertisementFromX28Dto createFromX28(Oste x28JobAdvertisement) {
        return new CreateJobAdvertisementFromX28Dto(
                sanitize(x28JobAdvertisement.getBezeichnung()),
                sanitize(x28JobAdvertisement.getBeschreibung()),
                x28JobAdvertisement.getFingerprint(),
                x28JobAdvertisement.getUrl(),
                createEmployment(x28JobAdvertisement),
                createCompany(x28JobAdvertisement),
                createLocation(x28JobAdvertisement),
                createOccupations(x28JobAdvertisement),
                createProfessionCodes(x28JobAdvertisement),
                parseDate(x28JobAdvertisement.getAnmeldeDatum()),
                parseDate(x28JobAdvertisement.getGueltigkeit())
        );
    }

    UpdateJobAdvertisementFromX28Dto updateFromX28(Oste x28JobAdvertisement) {
        return new UpdateJobAdvertisementFromX28Dto(
                x28JobAdvertisement.getStellennummerEGov(),
                x28JobAdvertisement.getFingerprint(),
                createProfessionCodes(x28JobAdvertisement)
        );
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

    private List<OccupationDto> createOccupations(Oste x28JobAdvertisement) {
        List<OccupationDto> occupations = new ArrayList<>();
        if (hasText(x28JobAdvertisement.getBq1AvamBerufNr())) {
            occupations.add(new OccupationDto(
                    x28JobAdvertisement.getBq1AvamBerufNr(),
                    AvamCodeResolver.EXPERIENCES.getRight(x28JobAdvertisement.getBq1ErfahrungCode()),
                    x28JobAdvertisement.getBq1AusbildungCode()
            ));
        }
        if (hasText(x28JobAdvertisement.getBq2AvamBerufNr())) {
            occupations.add(new OccupationDto(
                    x28JobAdvertisement.getBq2AvamBerufNr(),
                    AvamCodeResolver.EXPERIENCES.getRight(x28JobAdvertisement.getBq2ErfahrungCode()),
                    x28JobAdvertisement.getBq2AusbildungCode()
            ));
        }
        if (hasText(x28JobAdvertisement.getBq3AvamBerufNr())) {
            occupations.add(new OccupationDto(
                    x28JobAdvertisement.getBq3AvamBerufNr(),
                    AvamCodeResolver.EXPERIENCES.getRight(x28JobAdvertisement.getBq3ErfahrungCode()),
                    x28JobAdvertisement.getBq3AusbildungCode()
            ));
        }
        return occupations;
    }

    private CreateLocationDto createLocation(Oste x28JobAdvertisement) {
        CreateLocationDto createLocationDto = extractLocation(x28JobAdvertisement.getArbeitsortText());
        if (createLocationDto != null) {
            if (x28JobAdvertisement.getArbeitsortPlz() != null) {
                createLocationDto.setPostalCode(x28JobAdvertisement.getArbeitsortPlz());
            }
            if (LICHTENSTEIN_ISO_CODE.equals(x28JobAdvertisement.getArbeitsortKanton())) {
                createLocationDto.setCountryIsoCode(LICHTENSTEIN_ISO_CODE);
            }
        }
        return createLocationDto;
    }

    private CompanyDto createCompany(Oste x28JobAdvertisement) {
        return new CompanyDto(
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

    private EmploymentDto createEmployment(Oste x28JobAdvertisement) {
        LocalDate startDate = parseDate(x28JobAdvertisement.getStellenantritt());
        LocalDate endDate = parseDate(x28JobAdvertisement.getVertragsdauer());
        WorkingTimePercentage workingTimePercentage = WorkingTimePercentage.evaluate(x28JobAdvertisement.getPensumVon(), x28JobAdvertisement.getPensumBis());
        return new EmploymentDto(
                startDate,
                endDate,
                false,
                safeBoolean(x28JobAdvertisement.isAbSofort(), false),
                safeBoolean(x28JobAdvertisement.isUnbefristet(), (endDate != null)),
                workingTimePercentage.getMin(),
                workingTimePercentage.getMax(),
                null
        );
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

    private CreateLocationDto extractLocation(String localityText) {
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
                return new CreateLocationDto(null, city, postalCode, countryIsoCode);
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

    private boolean safeBoolean(Boolean value, boolean defaultValue) {
        return (value != null) ? value : defaultValue;
    }

}
