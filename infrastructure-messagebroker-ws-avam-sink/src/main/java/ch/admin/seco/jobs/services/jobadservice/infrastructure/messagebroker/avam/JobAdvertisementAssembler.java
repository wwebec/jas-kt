package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.*;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Location;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.TOsteEgov;

public class JobAdvertisementAssembler {

    private static final DateTimeFormatter AVAM_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static boolean safeBoolean(Boolean value) {
        return (value != null) ? value : false;
    }

    private static String safeToString(Object value) {
        return (value != null) ? value.toString() : null;
    }

    private static boolean isNotNull(Object value) {
        return (value != null);
    }

    public static String formatLocalDate(LocalDate localDate) {
        return (localDate != null) ? localDate.format(AVAM_DATE_FORMATTER) : null;
    }

    public TOsteEgov toOsteEgov(JobAdvertisement jobAdvertisement, AvamAction action) {
        TOsteEgov tOsteEgov = new TOsteEgov();
        tOsteEgov.setDetailangabenCode(AvamCodeResolver.ACTIONS.getLeft(action));
        tOsteEgov.setArbeitsamtBereich(jobAdvertisement.getJobCenterCode());
        tOsteEgov.setStellennummerEgov(jobAdvertisement.getId().getValue());
        tOsteEgov.setStellennummerAvam(jobAdvertisement.getStellennummerAvam());

        tOsteEgov.setAnmeldeDatum(formatLocalDate(jobAdvertisement.getApprovalDate()));
        if (AvamAction.ABMELDUNG.equals(action)) {
            tOsteEgov.setAbmeldeDatum(formatLocalDate(jobAdvertisement.getCancellationDate()));
            tOsteEgov.setAbmeldeGrundCode(jobAdvertisement.getCancellationCode());
        }
        tOsteEgov.setGueltigkeit(formatLocalDate(jobAdvertisement.getPublicationEndDate()));

        tOsteEgov.setEures(jobAdvertisement.isEures());
        tOsteEgov.setEuresAnonym(jobAdvertisement.isEuresAnonymous());

        tOsteEgov.setBezeichnung(jobAdvertisement.getTitle());
        tOsteEgov.setBeschreibung(jobAdvertisement.getDescription());

        fillEmployment(tOsteEgov, jobAdvertisement.getEmployment());
        tOsteEgov.setKategorieCode(AvamCodeResolver.DRIVING_LICENSE_LEVELS.getLeft(jobAdvertisement.getDrivingLicenseLevel()));

        fillApplyChannel(tOsteEgov, jobAdvertisement.getApplyChannel());
        fillCompany(tOsteEgov, jobAdvertisement.getCompany());
        fillContact(tOsteEgov, jobAdvertisement.getContact());
        fillLocation(tOsteEgov, jobAdvertisement.getLocation());
        fillOccupation(tOsteEgov, jobAdvertisement.getOccupations());
        fillLangaugeSkills(tOsteEgov, jobAdvertisement.getLanguageSkills());

        return tOsteEgov;
    }

    private void fillEmployment(TOsteEgov tOsteEgov, Employment employment) {
        if (employment == null) {
            return;
        }
        boolean immediately = safeBoolean(employment.getImmediately());
        tOsteEgov.setAbSofort(immediately);
        if (!immediately) {
            tOsteEgov.setStellenantritt(formatLocalDate(employment.getStartDate()));
        }
        boolean permanent = safeBoolean(employment.getPermanent());
        tOsteEgov.setUnbefristet(permanent);
        if (!permanent) {
            tOsteEgov.setVertragsdauer(formatLocalDate(employment.getEndDate()));
        }
        tOsteEgov.setPensumVon((short) employment.getWorkloadPercentageMin());
        tOsteEgov.setPensumBis((short) employment.getWorkloadPercentageMax());
    }

    private void fillApplyChannel(TOsteEgov tOsteEgov, ApplyChannel applyChannel) {
        if (applyChannel == null) {
            return;
        }
        tOsteEgov.setBewerSchriftlich(isNotNull(applyChannel.getMailAddress()));
        tOsteEgov.setBewerElektronisch(isNotNull(applyChannel.getEmailAddress()));
        tOsteEgov.setUntEmail(applyChannel.getEmailAddress());
        tOsteEgov.setUntUrl(applyChannel.getFormUrl()); // actually used for 'Online Bewerbung' instead 'home page'
        tOsteEgov.setBewerTelefonisch(isNotNull(applyChannel.getPhoneNumber()));
        tOsteEgov.setUntTelefon(applyChannel.getPhoneNumber());
        tOsteEgov.setBewerAngaben(applyChannel.getAdditionalInfo());
    }

    private void fillCompany(TOsteEgov tOsteEgov, Company company) {
        if (company == null) {
            return;
        }
        tOsteEgov.setUntName(company.getName());
        tOsteEgov.setUntStrasse(company.getStreet());
        tOsteEgov.setUntHausNr(company.getHouseNumber());
        tOsteEgov.setUntOrt(company.getCity());
        tOsteEgov.setUntPlz(company.getPostalCode());
        tOsteEgov.setUntPostfach(company.getPostOfficeBoxNumber());
        tOsteEgov.setUntPostfachPlz(company.getPostOfficeBoxPostalCode());
        tOsteEgov.setUntPostfachOrt(company.getPostOfficeBoxCity());
        tOsteEgov.setUntLand(company.getCountryIsoCode());
    }

    private void fillContact(TOsteEgov tOsteEgov, Contact contact) {
        if (contact == null) {
            return;
        }
        tOsteEgov.setKpAnredeCode(AvamCodeResolver.SALUTATIONS.getLeft(contact.getSalutation()));
        tOsteEgov.setKpVorname(contact.getFirstName());
        tOsteEgov.setKpName(contact.getLastName());
        tOsteEgov.setKpTelefonNr(contact.getPhone());
        tOsteEgov.setKpEmail(contact.getEmail());
    }

    private void fillLocation(TOsteEgov tOsteEgov, Location location) {
        if (location == null) {
            return;
        }
        tOsteEgov.setArbeitsOrtText(location.getRemarks());
        tOsteEgov.setArbeitsOrtOrt(location.getCity());
        tOsteEgov.setArbeitsOrtPlz(location.getPostalCode());
        tOsteEgov.setArbeitsOrtGemeinde(location.getCommunalCode());
        tOsteEgov.setArbeitsOrtLand(location.getCountryIsoCode());
    }

    private void fillOccupation(TOsteEgov tOsteEgov, List<Occupation> occupations) {
        if (occupations == null) {
            return;
        }
        if (occupations.size() > 0) {
            Occupation occupation = occupations.get(0);
            tOsteEgov.setBq1AvamBeruf(occupation.getLabel());
            tOsteEgov.setBq1ErfahrungCode(AvamCodeResolver.EXPERIENCES.getLeft(occupation.getWorkExperience()));
            tOsteEgov.setBq1AusbildungCode(occupation.getEducationCode());
        }
        /*
        if (occupations.size() > 1) {
            Occupation occupation = occupations.get(1);
            tOsteEgov.setBq2AvamBeruf(occupation.getLabel());
            tOsteEgov.setBq2ErfahrungCode(AvamCodeResolver.EXPERIENCES.getLeft(occupation.getWorkExperience()));
            tOsteEgov.setBq2AusbildungCode(occupation.getEducationCode());
        }
        if (occupations.size() > 2) {
            Occupation occupation = occupations.get(2);
            tOsteEgov.setBq3AvamBeruf(occupation.getLabel());
            tOsteEgov.setBq3ErfahrungCode(AvamCodeResolver.EXPERIENCES.getLeft(occupation.getWorkExperience()));
            tOsteEgov.setBq3AusbildungCode(occupation.getEducationCode());
        }
        */
    }

    private void fillLangaugeSkills(TOsteEgov tOsteEgov, List<LanguageSkill> languageSkills) {
        if (languageSkills == null) {
            return;
        }
        if (languageSkills.size() > 0) {
            LanguageSkill languageSkill = languageSkills.get(0);
            tOsteEgov.setSk1SpracheCode(AvamCodeResolver.LANGUAGES.getLeft(languageSkill.getLanguageIsoCode()));
            tOsteEgov.setSk1MuendlichCode(AvamCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getSpokenLevel()));
            tOsteEgov.setSk1SchriftlichCode(AvamCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getWrittenLevel()));
        }
        if (languageSkills.size() > 1) {
            LanguageSkill languageSkill = languageSkills.get(1);
            tOsteEgov.setSk2SpracheCode(AvamCodeResolver.LANGUAGES.getLeft(languageSkill.getLanguageIsoCode()));
            tOsteEgov.setSk2MuendlichCode(AvamCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getSpokenLevel()));
            tOsteEgov.setSk2SchriftlichCode(AvamCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getWrittenLevel()));
        }
        if (languageSkills.size() > 2) {
            LanguageSkill languageSkill = languageSkills.get(2);
            tOsteEgov.setSk3SpracheCode(AvamCodeResolver.LANGUAGES.getLeft(languageSkill.getLanguageIsoCode()));
            tOsteEgov.setSk3MuendlichCode(AvamCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getSpokenLevel()));
            tOsteEgov.setSk3SchriftlichCode(AvamCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getWrittenLevel()));
        }
        if (languageSkills.size() > 3) {
            LanguageSkill languageSkill = languageSkills.get(3);
            tOsteEgov.setSk4SpracheCode(AvamCodeResolver.LANGUAGES.getLeft(languageSkill.getLanguageIsoCode()));
            tOsteEgov.setSk4MuendlichCode(AvamCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getSpokenLevel()));
            tOsteEgov.setSk4SchriftlichCode(AvamCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getWrittenLevel()));
        }
        if (languageSkills.size() > 4) {
            LanguageSkill languageSkill = languageSkills.get(4);
            tOsteEgov.setSk5SpracheCode(AvamCodeResolver.LANGUAGES.getLeft(languageSkill.getLanguageIsoCode()));
            tOsteEgov.setSk5MuendlichCode(AvamCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getSpokenLevel()));
            tOsteEgov.setSk5SchriftlichCode(AvamCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getWrittenLevel()));
        }
    }
}
