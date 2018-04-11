package ch.admin.seco.jobs.services.jobadservice.integration.x28.importer.config;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.CompanyDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.EmploymentDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.OccupationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateLocationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.UpdateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.utils.WorkingTimePercentage;
import ch.admin.seco.jobs.services.jobadservice.integration.x28.jobadimport.Oste;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.util.StringUtils.hasText;
import static org.springframework.util.StringUtils.isEmpty;

class JobAdvertisementDtoAssembler {
    private static final String SWISS_ISO_CODE = "CH";
    private static final String LICHTENSTEIN_ISO_CODE = "LI";
    private static final String ORACLE_DATE_FORMAT = "yyyy-MM-dd-HH.mm.ss.SSSSSS";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(ORACLE_DATE_FORMAT);

    CreateJobAdvertisementFromX28Dto createFromX28(Oste x28JobAdvertisement) {
        return new CreateJobAdvertisementFromX28Dto(
                sanitize(x28JobAdvertisement.getBezeichnung()),
                sanitize(x28JobAdvertisement.getBeschreibung()),
                x28JobAdvertisement.getFingerprint(),
                x28JobAdvertisement.getUrl(),
                createEmploymentDto(x28JobAdvertisement),
                createCompanyDto(x28JobAdvertisement),
                createCreateLocationDto(x28JobAdvertisement),
                createOccupationDtos(x28JobAdvertisement),
                createProfessionCodes(x28JobAdvertisement.getBerufsBezeichnungen()));
    }

    UpdateJobAdvertisementFromX28Dto updateFromX28(Oste x28JobAdvertisement) {
        List<Integer> berufsBezeichnungen = x28JobAdvertisement.getBerufsBezeichnungen();
        String x28OccupationCodes = null;
        if (!berufsBezeichnungen.isEmpty()) {
            x28OccupationCodes = berufsBezeichnungen.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
        }

        return new UpdateJobAdvertisementFromX28Dto(
                x28JobAdvertisement.getStellennummerEGov(),
                x28JobAdvertisement.getFingerprint(),
                x28OccupationCodes
        );

    }

    private List<String> createProfessionCodes(List<Integer> professionCodes) {
        return professionCodes.stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

    private List<OccupationDto> createOccupationDtos(Oste x28JobAdvertisement) {
        return Stream.of(x28JobAdvertisement.getBq1AvamBerufNr(), x28JobAdvertisement.getBq2AvamBerufNr(), x28JobAdvertisement.getBq3AvamBerufNr())
                .filter(StringUtils::hasText)
                .map(avamOccupationCode -> new OccupationDto(avamOccupationCode, null, null))
                .collect(Collectors.toList());
    }

    private CreateLocationDto createCreateLocationDto(Oste x28JobAdvertisement) {
        if (LICHTENSTEIN_ISO_CODE.equals(x28JobAdvertisement.getArbeitsortKanton())) {
            return new CreateLocationDto(null, x28JobAdvertisement.getArbeitsortText(), x28JobAdvertisement.getArbeitsortPlz(), LICHTENSTEIN_ISO_CODE, null);
        }
        return new CreateLocationDto(null, x28JobAdvertisement.getArbeitsortText(), x28JobAdvertisement.getArbeitsortPlz(), SWISS_ISO_CODE, null);
    }

    private CompanyDto createCompanyDto(Oste x28JobAdvertisement) {
        return new CompanyDto(x28JobAdvertisement.getUntName(), null, null, null, null, null, null, null, null, null, null, null, false);
    }

    private EmploymentDto createEmploymentDto(Oste x28JobAdvertisement) {
        LocalDate startDate = parseDate(x28JobAdvertisement.getStellenantritt(), TimeMachine.now().toLocalDate());
        LocalDate endDate = parseDate(x28JobAdvertisement.getVertragsdauer(), null);
        WorkingTimePercentage workingTimePercentage = WorkingTimePercentage.evaluate(x28JobAdvertisement.getPensumVon(), x28JobAdvertisement.getPensumBis());
        return new EmploymentDto(
                startDate,
                endDate,
                false,
                safeBoolean(x28JobAdvertisement.isAbSofort(), !startDate.isAfter(TimeMachine.now().toLocalDate())),
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

    private LocalDate parseDate(String startDate, LocalDate defaultValue) {
        if (isEmpty(startDate)) {
            return defaultValue;
        }
        return LocalDate.parse(startDate, DATE_FORMATTER);
    }

    private boolean safeBoolean(Boolean value, boolean defaultValue) {
        return (value != null) ? value : defaultValue;
    }

}
