package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.*;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.TOsteEgov;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.WSArbeitsform;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.WSArbeitsformArray;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.AvamCodeResolver.SOURCE_SYSTEM;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.AvamCodeResolver.WORK_FORMS;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.AvamDateTimeFormatter.formatLocalDate;
import static org.springframework.util.StringUtils.hasText;

public class AvamJobAdvertisementAssembler {

    private static boolean safeBoolean(Boolean value) {
        return (value != null) ? value : false;
    }

    private static String tempMapCancellationCode(CancellationCode code) {
        String avamCode = AvamCodeResolver.CANCELLATION_CODE.getLeft(code);
        switch (avamCode) {
            case "6":
                return "5";
            case "0":
                return "7";
            default:
                return avamCode;
        }
    }

    public TOsteEgov toOsteEgov(JobAdvertisement jobAdvertisement, AvamAction action) {
        TOsteEgov avamJobAdvertisement = new TOsteEgov();
        avamJobAdvertisement.setDetailangabenCode(AvamCodeResolver.ACTIONS.getLeft(action));
        avamJobAdvertisement.setArbeitsamtBereich(jobAdvertisement.getJobCenterCode());
        avamJobAdvertisement.setStellennummerEgov(jobAdvertisement.getStellennummerEgov());
        avamJobAdvertisement.setStellennummerAvam(jobAdvertisement.getStellennummerAvam());
        avamJobAdvertisement.setArbeitsamtBereich(getJobCenterCode(jobAdvertisement.getJobCenterCode()));

        avamJobAdvertisement.setAnmeldeDatum(formatLocalDate(jobAdvertisement.getApprovalDate()));
        if (AvamAction.ABMELDUNG.equals(action)) {
            avamJobAdvertisement.setAbmeldeDatum(formatLocalDate(jobAdvertisement.getCancellationDate()));
            avamJobAdvertisement.setAbmeldeGrundCode(tempMapCancellationCode(jobAdvertisement.getCancellationCode()));
        }

        final Publication publication = jobAdvertisement.getPublication();
        //TODO: Review if we need to check nullability
        Assert.notNull(publication, "jobAdvertisement.getPublication can not be null");

        avamJobAdvertisement.setGueltigkeit(null);

        avamJobAdvertisement.setEures(publication.isEuresDisplay());
        avamJobAdvertisement.setEuresAnonym(publication.isEuresAnonymous());
        avamJobAdvertisement.setPublikation(publication.isPublicDisplay());
        avamJobAdvertisement.setAnonym(publication.isCompanyAnonymous());
        avamJobAdvertisement.setLoginPublikation(publication.isRestrictedDisplay());
        avamJobAdvertisement.setLoginAnonym(publication.isCompanyAnonymous());

        final JobContent jobContent = jobAdvertisement.getJobContent();
        //TODO: Review if we need to check nullability
        Assert.notNull(jobContent, "jobAdvertisement.getJobContent can not be null");

        //TODO: Check which description has to be use
        final JobDescription defaultJobDescription = jobContent.getJobDescriptions().stream()
                .findFirst()
                .orElse(null);

        //TODO: Review if we need to check nullability
        Assert.notNull(defaultJobDescription, "jobContent.getJobDescriptions can not be empty");

        avamJobAdvertisement.setBezeichnung(defaultJobDescription.getTitle());
        avamJobAdvertisement.setBeschreibung(defaultJobDescription.getDescription());

        avamJobAdvertisement.setGleicheOste(jobContent.getNumberOfJobs());
        fillEmployment(avamJobAdvertisement, jobContent.getEmployment());
        fillApplyChannel(avamJobAdvertisement, jobContent.getApplyChannel());
        fillCompany(avamJobAdvertisement, jobContent.getCompany());
        fillEmployer(avamJobAdvertisement, jobContent.getEmployer());
        fillContact(avamJobAdvertisement, jobAdvertisement.getContact());
        fillLocation(avamJobAdvertisement, jobContent.getLocation());
        fillOccupation(avamJobAdvertisement, jobContent.getOccupations());
        fillLangaugeSkills(avamJobAdvertisement, jobContent.getLanguageSkills());

        avamJobAdvertisement.setMeldepflicht(jobAdvertisement.isReportingObligation());
        avamJobAdvertisement.setSperrfrist(formatLocalDate(jobAdvertisement.getReportingObligationEndDate()));
        avamJobAdvertisement.setQuelleCode(SOURCE_SYSTEM.getLeft(jobAdvertisement.getSourceSystem()));

        return avamJobAdvertisement;
    }

    private void fillEmployment(TOsteEgov avamJobAdvertisement, Employment employment) {
        if (employment == null) {
            return;
        }
        boolean immediately = safeBoolean(employment.isImmediately());
        avamJobAdvertisement.setAbSofort(immediately);
        if (!immediately) {
            avamJobAdvertisement.setStellenantritt(formatLocalDate(employment.getStartDate()));
        }
        boolean permanent = safeBoolean(employment.isPermanent());
        avamJobAdvertisement.setUnbefristet(permanent);
        if (!permanent) {
            avamJobAdvertisement.setVertragsdauer(formatLocalDate(employment.getEndDate()));
        }
        avamJobAdvertisement.setKurzeinsatz(employment.isShortEmployment());

        avamJobAdvertisement.setPensumVon((short) employment.getWorkloadPercentageMin());
        avamJobAdvertisement.setPensumBis((short) employment.getWorkloadPercentageMax());

        if (employment.getWorkForms() != null) {
            List<WSArbeitsform> wsArbeitsformList = employment.getWorkForms()
                    .stream()
                    .map(WORK_FORMS::getLeft)
                    .map(arbeitsformCode -> {
                        WSArbeitsform wsArbeitsform = new WSArbeitsform();
                        wsArbeitsform.setArbeitsformCode(arbeitsformCode);
                        return wsArbeitsform;
                    })
                    .collect(Collectors.toList());

            WSArbeitsformArray wsArbeitsformArray = new WSArbeitsformArray();
            wsArbeitsformArray.getWSArbeitsformArrayItem().addAll(wsArbeitsformList);
            avamJobAdvertisement.setArbeitsformCodeList(wsArbeitsformArray);
        }
    }

    private void fillApplyChannel(TOsteEgov avamJobAdvertisement, ApplyChannel applyChannel) {
        if (applyChannel == null) {
            return;
        }
        avamJobAdvertisement.setBewerSchriftlich(hasText(applyChannel.getMailAddress()));
        avamJobAdvertisement.setBewerElektronisch(hasText(applyChannel.getEmailAddress()) || hasText(applyChannel.getFormUrl()));
        avamJobAdvertisement.setUntEmail(applyChannel.getEmailAddress());
        avamJobAdvertisement.setUntUrl(applyChannel.getFormUrl()); // actually used for 'Online Bewerbung' instead 'home page'
        avamJobAdvertisement.setBewerTelefonisch(hasText(applyChannel.getPhoneNumber()));
        avamJobAdvertisement.setUntTelefon(applyChannel.getPhoneNumber());
        avamJobAdvertisement.setBewerAngaben(applyChannel.getAdditionalInfo());
    }

    private void fillCompany(TOsteEgov avamJobAdvertisement, Company company) {
        if (company == null) {
            return;
        }
        avamJobAdvertisement.setUntName(company.getName());
        avamJobAdvertisement.setUntStrasse(company.getStreet());
        avamJobAdvertisement.setUntHausNr(company.getHouseNumber());
        avamJobAdvertisement.setUntOrt(company.getCity());
        avamJobAdvertisement.setUntPlz(company.getPostalCode());
        avamJobAdvertisement.setUntPostfach(company.getPostOfficeBoxNumber());
        avamJobAdvertisement.setUntPostfachPlz(company.getPostOfficeBoxPostalCode());
        avamJobAdvertisement.setUntPostfachOrt(company.getPostOfficeBoxCity());
        avamJobAdvertisement.setUntLand(company.getCountryIsoCode());
        avamJobAdvertisement.setAuftraggeber(company.isSurrogate());
    }

    private void fillEmployer(TOsteEgov avamJobAdvertisement, Employer employer) {
        if (employer == null) {
            return;
        }
        avamJobAdvertisement.setAuftraggeberName(employer.getName());
        avamJobAdvertisement.setAuftraggeberPlz(employer.getPostalCode());
        avamJobAdvertisement.setAuftraggeberOrt(employer.getCity());
        avamJobAdvertisement.setAuftraggeberLand(employer.getCountryIsoCode());
    }

    private void fillContact(TOsteEgov avamJobAdvertisement, Contact contact) {
        if (contact == null) {
            return;
        }
        avamJobAdvertisement.setKpAnredeCode(AvamCodeResolver.SALUTATIONS.getLeft(contact.getSalutation()));
        avamJobAdvertisement.setKpVorname(contact.getFirstName());
        avamJobAdvertisement.setKpName(contact.getLastName());
        avamJobAdvertisement.setKpTelefonNr(contact.getPhone());
        // FIXME: Temparory fix for mulitple email-addresses. to be remove after 01.09.2018 or handled otherwise
        avamJobAdvertisement.setKpEmail(fetchFirstEmail(contact.getEmail()));
        //avamJobAdvertisement.setKpEmail(contact.getEmail());
    }

    static String fetchFirstEmail(String email) {
        if (hasText(email)) {
            String[] tokens = email.split(",\\s*");
            return tokens[0];
        }
        return null;
    }

    private void fillLocation(TOsteEgov avamJobAdvertisement, Location location) {
        if (location == null) {
            return;
        }
        avamJobAdvertisement.setArbeitsOrtText(location.getRemarks());
        avamJobAdvertisement.setArbeitsOrtOrt(location.getCity());
        avamJobAdvertisement.setArbeitsOrtPlz(location.getPostalCode());
        //avamJobAdvertisement.setArbeitsOrtGemeindeNr(location.getCommunalCode());
        avamJobAdvertisement.setArbeitsOrtLand(location.getCountryIsoCode());
    }

    private void fillOccupation(TOsteEgov avamJobAdvertisement, List<Occupation> occupations) {
        if (occupations == null) {
            return;
        }
        if (occupations.size() > 0) {
            Occupation occupation = occupations.get(0);
            avamJobAdvertisement.setBq1AvamBeruf(occupation.getLabel());
            avamJobAdvertisement.setBq1AvamBerufNr(occupation.getAvamOccupationCode());
            avamJobAdvertisement.setBq1ErfahrungCode(AvamCodeResolver.EXPERIENCES.getLeft(occupation.getWorkExperience()));
            avamJobAdvertisement.setBq1AusbildungCode(occupation.getEducationCode());
        }
        /*
        if (occupations.size() > 1) {
            Occupation occupation = occupations.get(1);
            tOsteEgov.setBq2AvamBeruf(occupation.getLabel());
            tOsteEgov.setBq2AvamBerufNr(occupation.getAvamOccupationCode());
            tOsteEgov.setBq2ErfahrungCode(AvamCodeResolver.EXPERIENCES.getLeft(occupation.getWorkExperience()));
            tOsteEgov.setBq2AusbildungCode(occupation.getEducationCode());
        }
        if (occupations.size() > 2) {
            Occupation occupation = occupations.get(2);
            tOsteEgov.setBq3AvamBeruf(occupation.getLabel());
            tOsteEgov.setBq3AvamBerufNr(occupation.getAvamOccupationCode());
            tOsteEgov.setBq3ErfahrungCode(AvamCodeResolver.EXPERIENCES.getLeft(occupation.getWorkExperience()));
            tOsteEgov.setBq3AusbildungCode(occupation.getEducationCode());
        }
        */
    }

    private void fillLangaugeSkills(TOsteEgov avamJobAdvertisement, List<LanguageSkill> languageSkills) {
        if (languageSkills == null) {
            return;
        }
        if (languageSkills.size() > 0) {
            LanguageSkill languageSkill = languageSkills.get(0);
            avamJobAdvertisement.setSk1SpracheCode(AvamCodeResolver.LANGUAGES.getLeft(languageSkill.getLanguageIsoCode()));
            avamJobAdvertisement.setSk1MuendlichCode(AvamCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getSpokenLevel()));
            avamJobAdvertisement.setSk1SchriftlichCode(AvamCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getWrittenLevel()));
        }
        if (languageSkills.size() > 1) {
            LanguageSkill languageSkill = languageSkills.get(1);
            avamJobAdvertisement.setSk2SpracheCode(AvamCodeResolver.LANGUAGES.getLeft(languageSkill.getLanguageIsoCode()));
            avamJobAdvertisement.setSk2MuendlichCode(AvamCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getSpokenLevel()));
            avamJobAdvertisement.setSk2SchriftlichCode(AvamCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getWrittenLevel()));
        }
        if (languageSkills.size() > 2) {
            LanguageSkill languageSkill = languageSkills.get(2);
            avamJobAdvertisement.setSk3SpracheCode(AvamCodeResolver.LANGUAGES.getLeft(languageSkill.getLanguageIsoCode()));
            avamJobAdvertisement.setSk3MuendlichCode(AvamCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getSpokenLevel()));
            avamJobAdvertisement.setSk3SchriftlichCode(AvamCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getWrittenLevel()));
        }
        if (languageSkills.size() > 3) {
            LanguageSkill languageSkill = languageSkills.get(3);
            avamJobAdvertisement.setSk4SpracheCode(AvamCodeResolver.LANGUAGES.getLeft(languageSkill.getLanguageIsoCode()));
            avamJobAdvertisement.setSk4MuendlichCode(AvamCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getSpokenLevel()));
            avamJobAdvertisement.setSk4SchriftlichCode(AvamCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getWrittenLevel()));
        }
        if (languageSkills.size() > 4) {
            LanguageSkill languageSkill = languageSkills.get(4);
            avamJobAdvertisement.setSk5SpracheCode(AvamCodeResolver.LANGUAGES.getLeft(languageSkill.getLanguageIsoCode()));
            avamJobAdvertisement.setSk5MuendlichCode(AvamCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getSpokenLevel()));
            avamJobAdvertisement.setSk5SchriftlichCode(AvamCodeResolver.LANGUAGE_LEVEL.getLeft(languageSkill.getWrittenLevel()));
        }
    }

    private String getJobCenterCode(String jobCenterCode) {
        if (jobCenterCode != null) {
            return jobCenterCode;
        }
        //TODO define default RAV if information is missing
        return "BE01";
    }
}
