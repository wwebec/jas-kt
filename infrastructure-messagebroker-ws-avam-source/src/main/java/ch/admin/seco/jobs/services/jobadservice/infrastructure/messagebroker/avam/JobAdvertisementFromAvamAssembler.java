package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.AvamCodeResolver.EXPERIENCES;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.AvamCodeResolver.LANGUAGES;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.AvamCodeResolver.LANGUAGE_LEVEL;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.AvamCodeResolver.SALUTATIONS;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.AvamDateTimeFormatter.parseToLocalDate;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.ApplyChannel;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Company;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Contact;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Employment;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageSkill;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Location;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Occupation;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.ApproveJobAdvertisementMessage;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.CancelJobAdvertisementMessage;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.RejectJobAdvertisementMessage;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.UpdateJobAdvertisementMessage;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.source.WSOsteEgov;

public class JobAdvertisementFromAvamAssembler {

    ApproveJobAdvertisementMessage createApproveJobAdvertisement(WSOsteEgov oste) {
        ApproveJobAdvertisementMessage approveJobAdvertisement = new ApproveJobAdvertisementMessage();
        approveJobAdvertisement.setStellennummerEgov(oste.getStellennummerEgov());
        approveJobAdvertisement.setOccupations(fillOccupations(oste));
        approveJobAdvertisement.setReportingObligation(true); // TODO set value form AVAM
        approveJobAdvertisement.setReportingObligationEndDate(parseToLocalDate(null)); // TODO set value form AVAM
        return approveJobAdvertisement;
    }

    RejectJobAdvertisementMessage createRejectJobAdvertisement(WSOsteEgov oste) {
        RejectJobAdvertisementMessage rejectJobAdvertisement = new RejectJobAdvertisementMessage();
        rejectJobAdvertisement.setStellennummerEgov(oste.getStellennummerEgov());
        rejectJobAdvertisement.setRejectionCode(oste.getAbmeldeGrundCode());
        rejectJobAdvertisement.setRejectionDate(parseToLocalDate(oste.getAbmeldeDatum()));
        return rejectJobAdvertisement;
    }

    UpdateJobAdvertisementMessage createUpdateJobAdvertisement(WSOsteEgov oste) {
        UpdateJobAdvertisementMessage updateJobAdvertisement = new UpdateJobAdvertisementMessage();
        updateJobAdvertisement.setSourceSystem(SourceSystem.RAV);
        updateJobAdvertisement.setStellennummerAvam(oste.getStellennummerAvam());
        updateJobAdvertisement.setReportingObligation(true); // TODO set value form AVAM
        updateJobAdvertisement.setReportingObligationEndDate(parseToLocalDate(null)); // TODO set value form AVAM
        updateJobAdvertisement.setTitle(oste.getBezeichnung());
        updateJobAdvertisement.setDescription(oste.getBeschreibung());
        updateJobAdvertisement.setJobCenterCode(oste.getArbeitsamtBereich());
        updateJobAdvertisement.setRavRegistrationDate(parseToLocalDate(oste.getAnmeldeDatum()));
        updateJobAdvertisement.setPublicationEndDate(parseToLocalDate(oste.getGueltigkeit()));
        updateJobAdvertisement.setEures(safeBoolean(oste.isEures()));
        updateJobAdvertisement.setEuresAnonymous(safeBoolean(oste.isEuresAnonym()));
        updateJobAdvertisement.setExternalUrl(oste.getUntUrl());
        updateJobAdvertisement.setContact(new Contact(
                SALUTATIONS.getRight(oste.getKpAnredeCode()),
                oste.getKpVorname(),
                oste.getKpName(),
                oste.getKpTelefonNr(),
                oste.getKpEmail()));
        updateJobAdvertisement.setCompany(new Company.Builder()
                .setName(oste.getUntName())
                .setStreet(oste.getUntStrasse())
                .setHouseNumber(oste.getUntHausNr())
                .setPostalCode(oste.getUntPlz())
                .setCity(oste.getUntOrt())
                .setCountryIsoCode(oste.getUntLand()) // FIXME value might be NULL
                .setPostOfficeBoxNumber(oste.getUntPostfach())
                .setPostOfficeBoxPostalCode(oste.getUntPostfachPlz())
                .setPostOfficeBoxCity(oste.getUntPostfachOrt())
                .build());
        updateJobAdvertisement.setEmployment(new Employment(
                parseToLocalDate(oste.getStellenantritt()),
                parseToLocalDate(oste.getVertragsdauer()),
                null,
                safeBoolean(oste.isAbSofort()),
                safeBoolean(oste.isUnbefristet()),
                oste.getPensumVon(),
                oste.getPensumBis()
        ));
        updateJobAdvertisement.setLocation(new Location(
                oste.getArbeitsOrtText(),
                oste.getArbeitsOrtOrt(),
                oste.getArbeitsOrtPlz(),
                String.valueOf(oste.getArbeitsOrtGemeindeNr()),
                null,
                null,
                oste.getArbeitsOrtLand(),
                null));
        updateJobAdvertisement.setLanguageSkills(fillLangaugeSkills(oste));
        updateJobAdvertisement.setOccupations(fillOccupations(oste));
        return updateJobAdvertisement;
    }

    CancelJobAdvertisementMessage createCancelJobAdvertisement(WSOsteEgov oste) {
        CancelJobAdvertisementMessage cancelJobAdvertisement = new CancelJobAdvertisementMessage();
        cancelJobAdvertisement.setStellennummerAvam(oste.getStellennummerAvam());
        cancelJobAdvertisement.setCancellationCode(oste.getAbmeldeGrundCode());
        cancelJobAdvertisement.setCancellationDate(parseToLocalDate(oste.getAbmeldeDatum()));
        return cancelJobAdvertisement;
    }

    private List<Occupation> fillOccupations(WSOsteEgov oste) {
        List<Occupation> occupationList = new ArrayList<>();
        fillOccupation(occupationList, oste.getBq1AvamBerufNr(), oste.getBq1ErfahrungCode(), oste.getBq1AusbildungCode());
        fillOccupation(occupationList, oste.getBq2AvamBerufNr(), oste.getBq2ErfahrungCode(), oste.getBq2AusbildungCode());
        fillOccupation(occupationList, oste.getBq2AvamBerufNr(), oste.getBq3ErfahrungCode(), oste.getBq2AusbildungCode());

        return occupationList;
    }

    private boolean safeBoolean(Boolean value) {
        return (value != null) ? value : false;
    }

    private ApplyChannel fillApplyChannel(WSOsteEgov oste) {
        return new ApplyChannel(
                oste.isBewerSchriftlich() ? "true" : null,
                oste.isBewerElektronisch() ? oste.getUntEmail() : null,
                oste.isBewerTelefonisch() ? oste.getUntTelefon() : null,
                oste.isBewerElektronisch() ? oste.getUntUrl() : null,
                oste.getBewerAngaben());
    }

    private SourceSystem getSourceSystem(WSOsteEgov oste) {
        return (hasText(oste.getStellennummerEgov())) ? SourceSystem.JOBROOM : SourceSystem.RAV;
    }

    private List<LanguageSkill> fillLangaugeSkills(WSOsteEgov oste) {
        List<LanguageSkill> languageSkills = new ArrayList<>();
        fillLanguageSkill(languageSkills, oste.getSk1SpracheCode(), oste.getSk1MuendlichCode(), oste.getSk1SchriftlichCode());
        fillLanguageSkill(languageSkills, oste.getSk2SpracheCode(), oste.getSk2MuendlichCode(), oste.getSk2SchriftlichCode());
        fillLanguageSkill(languageSkills, oste.getSk3SpracheCode(), oste.getSk3MuendlichCode(), oste.getSk3SchriftlichCode());
        fillLanguageSkill(languageSkills, oste.getSk4SpracheCode(), oste.getSk4MuendlichCode(), oste.getSk4SchriftlichCode());
        fillLanguageSkill(languageSkills, oste.getSk5SpracheCode(), oste.getSk5MuendlichCode(), oste.getSk5SchriftlichCode());
        return languageSkills;
    }

    private void fillLanguageSkill(List<LanguageSkill> languageSkills, String sprachCode, String muendlichCode, String schriftlichCode) {
        if (hasText(sprachCode)) {
            languageSkills.add(new LanguageSkill(
                    LANGUAGES.getRight(sprachCode),
                    LANGUAGE_LEVEL.getRight(muendlichCode),
                    LANGUAGE_LEVEL.getRight(schriftlichCode)
            ));
        }
    }

    private void fillOccupation(List<Occupation> occupationList, BigInteger avamBerufNummer, String erfahrungsCode, String ausbildungsCode) {
        if (nonNull(avamBerufNummer)) {
            occupationList.add(new Occupation(
                            avamBerufNummer.toString(),
                            EXPERIENCES.getRight(erfahrungsCode),
                            ausbildungsCode
                    )
            );
        }
    }
}
