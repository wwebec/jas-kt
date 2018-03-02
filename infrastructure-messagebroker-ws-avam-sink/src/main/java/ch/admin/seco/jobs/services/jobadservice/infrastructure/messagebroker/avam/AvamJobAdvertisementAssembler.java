package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

import org.springframework.util.CollectionUtils;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.ApplyChannel;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Company;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Contact;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageSkill;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Locality;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Occupation;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.Profession;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionCode;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionCodeType;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.TOsteEgov;

public class AvamJobAdvertisementAssembler {

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

    /**
     * FIXME:
     * setBq1AvamBeruf resolve by occupation.getProfessionId()
     * setBq2AvamBeruf resolve by occupation.getProfessionId()
     * setBq3AvamBeruf resolve by occupation.getProfessionId()
     *
     * @param jobAdvertisement object from the domain
     * @param action
     * @return the object for the ws
     */
    public TOsteEgov toOsteEgov(JobAdvertisement jobAdvertisement, List<Profession> professions, AvamAction action) {
        TOsteEgov tOsteEgov = new TOsteEgov();
        tOsteEgov.setDetailangabenCode(AvamAwesomeCodeResolver.ACTIONS.getLeft(action));
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

        fillEmployment(tOsteEgov, jobAdvertisement);
        tOsteEgov.setKategorieCode(AvamAwesomeCodeResolver.DRIVING_LICENSE_LEVELS.getLeft(jobAdvertisement.getDrivingLicenseLevel()));

        fillApplyChannel(tOsteEgov, jobAdvertisement.getApplyChannel());
        fillCompany(tOsteEgov, jobAdvertisement.getCompany());
        fillContact(tOsteEgov, jobAdvertisement.getContact());
        fillLocalities(tOsteEgov, jobAdvertisement.getLocalities());
        fillOccupation(tOsteEgov, jobAdvertisement.getOccupations(), professions);
        tOsteEgov.setBq1AusbildungCode(jobAdvertisement.getEducationCode());
        fillLangaugeSkills(tOsteEgov, jobAdvertisement.getLanguageSkills());

        return tOsteEgov;
    }

    private void fillEmployment(TOsteEgov tOsteEgov, JobAdvertisement jobAdvertisement) {
        boolean immediately = safeBoolean(jobAdvertisement.getImmediately());
        tOsteEgov.setAbSofort(immediately);
        if (!immediately) {
            tOsteEgov.setStellenantritt(formatLocalDate(jobAdvertisement.getEmploymentStartDate()));
        }
        boolean permanent = safeBoolean(jobAdvertisement.getPermanent());
        tOsteEgov.setUnbefristet(permanent);
        if (!permanent) {
            tOsteEgov.setVertragsdauer(formatLocalDate(jobAdvertisement.getEmploymentEndDate()));
        }
        tOsteEgov.setPensumVon((short) jobAdvertisement.getWorkloadPercentageMin());
        tOsteEgov.setPensumBis((short) jobAdvertisement.getWorkloadPercentageMax());
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
        tOsteEgov.setUntPlz(company.getZipCode());
        tOsteEgov.setUntPostfach(company.getPostOfficeBoxNumber());
        tOsteEgov.setUntPostfachPlz(company.getPostOfficeBoxZipCode());
        tOsteEgov.setUntPostfachOrt(company.getPostOfficeBoxCity());
        tOsteEgov.setUntLand(company.getCountryIsoCode());
    }

    private void fillContact(TOsteEgov tOsteEgov, Contact contact) {
        if (contact == null) {
            return;
        }
        tOsteEgov.setKpAnredeCode(AvamAwesomeCodeResolver.SALUTATIONS.getLeft(contact.getSalutation()));
        tOsteEgov.setKpVorname(contact.getFirstName());
        tOsteEgov.setKpName(contact.getLastName());
        tOsteEgov.setKpTelefonNr(contact.getPhone());
        tOsteEgov.setKpEmail(contact.getEmail());
    }

    private void fillLocalities(TOsteEgov tOsteEgov, List<Locality> localities) {
        if (localities == null) {
            return;
        }
        if (localities.size() > 0) {
            Locality locality = localities.get(0);
            tOsteEgov.setArbeitsOrtText(locality.getRemarks());
            tOsteEgov.setArbeitsOrtOrt(locality.getCity());
            tOsteEgov.setArbeitsOrtPlz(locality.getZipCode());
            tOsteEgov.setArbeitsOrtGemeinde(locality.getCommunalCode());
            tOsteEgov.setArbeitsOrtLand(locality.getCountryIsoCode());
        }
    }

    private void fillOccupation(TOsteEgov tOsteEgov, List<Occupation> occupations, List<Profession> professions) {
        if (occupations == null) {
            return;
        }
        if (occupations.size() > 0) {
            Occupation occupation = occupations.get(0);
            tOsteEgov.setBq1AvamBeruf(getProfessionLabel(professions, occupation));
            tOsteEgov.setBq1ErfahrungCode(AvamAwesomeCodeResolver.EXPERIENCES.getLeft(occupation.getWorkExperience()));
        }
        if (occupations.size() > 1) {
            Occupation occupation = occupations.get(1);
            tOsteEgov.setBq2AvamBeruf(getProfessionLabel(professions, occupation));
            tOsteEgov.setBq2ErfahrungCode(AvamAwesomeCodeResolver.EXPERIENCES.getLeft(occupation.getWorkExperience()));
        }
        if (occupations.size() > 2) {
            Occupation occupation = occupations.get(2);
            tOsteEgov.setBq3AvamBeruf(getProfessionLabel(professions, occupation));
            tOsteEgov.setBq3ErfahrungCode(AvamAwesomeCodeResolver.EXPERIENCES.getLeft(occupation.getWorkExperience()));
        }
    }

    private String getProfessionLabel(List<Profession> professions, Occupation occupation) {
        if (!CollectionUtils.isEmpty(professions)) {
            professions.stream()
                    .filter(profession -> occupation.getProfessionId().equals(profession.getId()))
                    .map(Profession::getCodes)
                    .flatMap(Collection::stream)
                    .filter(professionCode -> ProfessionCodeType.AVAM.equals(professionCode.getType()))
                    .findFirst()
                    .map(ProfessionCode::getLabel)
                    .orElse(null);
        }
        return null;
    }

    private void fillLangaugeSkills(TOsteEgov tOsteEgov, List<LanguageSkill> languageSkills) {
        if (languageSkills == null) {
            return;
        }
        if (languageSkills.size() > 0) {
            LanguageSkill languageSkill = languageSkills.get(0);
            tOsteEgov.setSk1SpracheCode(AvamAwesomeCodeResolver.LANGUAGES.getLeft(languageSkill.getLanguageIsoCode()));
            tOsteEgov.setSk1MuendlichCode(AvamAwesomeCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getSpokenLevel()));
            tOsteEgov.setSk1SchriftlichCode(AvamAwesomeCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getWrittenLevel()));
        }
        if (languageSkills.size() > 1) {
            LanguageSkill languageSkill = languageSkills.get(1);
            tOsteEgov.setSk2SpracheCode(AvamAwesomeCodeResolver.LANGUAGES.getLeft(languageSkill.getLanguageIsoCode()));
            tOsteEgov.setSk2MuendlichCode(AvamAwesomeCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getSpokenLevel()));
            tOsteEgov.setSk2SchriftlichCode(AvamAwesomeCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getWrittenLevel()));
        }
        if (languageSkills.size() > 2) {
            LanguageSkill languageSkill = languageSkills.get(2);
            tOsteEgov.setSk3SpracheCode(AvamAwesomeCodeResolver.LANGUAGES.getLeft(languageSkill.getLanguageIsoCode()));
            tOsteEgov.setSk3MuendlichCode(AvamAwesomeCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getSpokenLevel()));
            tOsteEgov.setSk3SchriftlichCode(AvamAwesomeCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getWrittenLevel()));
        }
        if (languageSkills.size() > 3) {
            LanguageSkill languageSkill = languageSkills.get(3);
            tOsteEgov.setSk4SpracheCode(AvamAwesomeCodeResolver.LANGUAGES.getLeft(languageSkill.getLanguageIsoCode()));
            tOsteEgov.setSk4MuendlichCode(AvamAwesomeCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getSpokenLevel()));
            tOsteEgov.setSk4SchriftlichCode(AvamAwesomeCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getWrittenLevel()));
        }
        if (languageSkills.size() > 4) {
            LanguageSkill languageSkill = languageSkills.get(4);
            tOsteEgov.setSk5SpracheCode(AvamAwesomeCodeResolver.LANGUAGES.getLeft(languageSkill.getLanguageIsoCode()));
            tOsteEgov.setSk5MuendlichCode(AvamAwesomeCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getSpokenLevel()));
            tOsteEgov.setSk5SchriftlichCode(AvamAwesomeCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getWrittenLevel()));
        }
    }

}
