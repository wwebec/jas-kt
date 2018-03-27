package ch.admin.seco.jobs.services.jobadservice.integration.x28.importer.config;

import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;
import static org.springframework.util.StringUtils.isEmpty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import org.springframework.util.StringUtils;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.ApplyChannelDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.CompanyDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.CreateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.CreateLocationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.EmploymentDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.OccupationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.UpdateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.integration.x28.jobadimport.Oste;

class ActionDtoFactory {
    private static final String SWISS_ISO_CODE = "CH";
    private static final String LICHTENSTEIN_ISO_CODE = "LI";
    private static final String ORACLE_DATE_FORMAT = "yyyy-MM-dd-HH.mm.ss.SSSSSS";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(ORACLE_DATE_FORMAT);

    CreateJobAdvertisementFromX28Dto createFromX28(Oste x28JobAdvertisement) {
        return new CreateJobAdvertisementFromX28Dto(
                sanitize(x28JobAdvertisement.getBezeichnung()),
                sanitize(x28JobAdvertisement.getBeschreibung()),
                x28JobAdvertisement.getFingerprint(),
                createEmploymentDto(x28JobAdvertisement),
                createApplyChannelDto(x28JobAdvertisement),
                createCompanyDto(x28JobAdvertisement),
                createCreateLocationDto(x28JobAdvertisement),
                createOccupationDtos(x28JobAdvertisement));
    }

    UpdateJobAdvertisementFromX28Dto updateFromX28(Oste x28JobAdvertisement) {
        return new UpdateJobAdvertisementFromX28Dto(x28JobAdvertisement.getStellennummerEGov(),
                x28JobAdvertisement.getFingerprint(),
                x28JobAdvertisement.getBerufsBezeichnungen().stream().findFirst()
                        .map(String::valueOf)
                        .orElse(null));

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

    private Collection<OccupationDto> createOccupationDtos(Oste x28JobAdvertisement) {
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
        return new CompanyDto(x28JobAdvertisement.getUntName(), null, null, null, null, null, null, null, null, null, null, null);
    }

    private ApplyChannelDto createApplyChannelDto(Oste x28JobAdvertisement) {
        return new ApplyChannelDto(null, null, null, x28JobAdvertisement.getUrl(), null);
    }

    private EmploymentDto createEmploymentDto(Oste x28JobAdvertisement) {
        LocalDate startDate = parseStartDate(x28JobAdvertisement.getAnmeldeDatum());
        return new EmploymentDto(
                startDate,
                startDate.plusDays(60), // default lifetime of a JobAdvertisement is 60 days
                null,
                null,
                x28JobAdvertisement.isUnbefristet(),
                nonNull(x28JobAdvertisement.getPensumVon()) ? x28JobAdvertisement.getPensumVon().intValue() : 100,
                nonNull(x28JobAdvertisement.getPensumBis()) ? x28JobAdvertisement.getPensumBis().intValue() : 100
        );
    }

    private LocalDate parseStartDate(String startDate) {
        if (isEmpty(startDate)) {
            return LocalDate.now();
        }
        return LocalDate.parse(startDate, DATE_FORMATTER);
    }
}
