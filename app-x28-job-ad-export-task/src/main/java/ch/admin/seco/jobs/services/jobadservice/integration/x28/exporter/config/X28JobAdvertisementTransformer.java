package ch.admin.seco.jobs.services.jobadservice.integration.x28.exporter.config;

import static org.springframework.util.CollectionUtils.isEmpty;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Employment;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobContent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobDescription;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageSkill;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Location;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Occupation;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.PublicContact;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Publication;
import ch.admin.seco.jobs.services.jobadservice.integration.x28.jobadexport.Oste;

public class X28JobAdvertisementTransformer implements ItemProcessor<JobAdvertisement, Oste> {
    private static final String ORACLE_DATE_FORMAT = "yyyy-MM-dd-HH.mm.ss.SSSSSS";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(ORACLE_DATE_FORMAT);

    @Override
    public Oste process(JobAdvertisement jobAdvertisement) {

        JobContent jobContent = jobAdvertisement.getJobContent();
        Oste x28JobAdvertisement = new Oste();
        x28JobAdvertisement.setOsteId(jobAdvertisement.getId().getValue());
        x28JobAdvertisement.setStellennummerAvam(jobAdvertisement.getStellennummerAvam());
        x28JobAdvertisement.setStellennummerEGov(jobAdvertisement.getStellennummerEgov());
        x28JobAdvertisement.setFingerprint(jobAdvertisement.getFingerprint());
        x28JobAdvertisement.setArbeitsamtbereich(jobAdvertisement.getJobCenterCode());
        mapTitleAndDescription(jobContent.getJobDescriptions(), x28JobAdvertisement);
        mapOccupation(jobContent.getOccupations(), x28JobAdvertisement);
        mapLocation(jobContent.getLocation(), x28JobAdvertisement);
        mapPublication(jobAdvertisement.getPublication(), x28JobAdvertisement);
        mapEmployment(jobContent.getEmployment(), x28JobAdvertisement);
        mapPublicContact(jobContent.getPublicContact(), x28JobAdvertisement);
        mapLanguageSkills(jobContent.getLanguageSkills(), x28JobAdvertisement);
        return x28JobAdvertisement;
    }

    private void mapLanguageSkills(List<LanguageSkill> languageSkills, Oste x28JobAdvertisement) {
        if (!isEmpty(languageSkills)) {
            if (languageSkills.size() >= 1) {
                LanguageSkill languageSkill = languageSkills.get(0);
                x28JobAdvertisement.setSk1SpracheCode(languageSkill.getLanguageIsoCode());
                x28JobAdvertisement.setSk1SchriftlichCode(languageSkill.getWrittenLevel().name());
                x28JobAdvertisement.setSk1MuendlichCode(languageSkill.getSpokenLevel().name());
            }
            if (languageSkills.size() >= 2) {
                LanguageSkill languageSkill = languageSkills.get(1);
                x28JobAdvertisement.setSk2SpracheCode(languageSkill.getLanguageIsoCode());
                x28JobAdvertisement.setSk2SchriftlichCode(languageSkill.getWrittenLevel().name());
                x28JobAdvertisement.setSk2MuendlichCode(languageSkill.getSpokenLevel().name());
            }
            if (languageSkills.size() >= 3) {
                LanguageSkill languageSkill = languageSkills.get(2);
                x28JobAdvertisement.setSk3SpracheCode(languageSkill.getLanguageIsoCode());
                x28JobAdvertisement.setSk3SchriftlichCode(languageSkill.getWrittenLevel().name());
                x28JobAdvertisement.setSk3MuendlichCode(languageSkill.getSpokenLevel().name());
            }
            if (languageSkills.size() >= 4) {
                LanguageSkill languageSkill = languageSkills.get(3);
                x28JobAdvertisement.setSk4SpracheCode(languageSkill.getLanguageIsoCode());
                x28JobAdvertisement.setSk4SchriftlichCode(languageSkill.getWrittenLevel().name());
                x28JobAdvertisement.setSk4MuendlichCode(languageSkill.getSpokenLevel().name());
            }
            if (languageSkills.size() >= 5) {
                LanguageSkill languageSkill = languageSkills.get(4);
                x28JobAdvertisement.setSk5SpracheCode(languageSkill.getLanguageIsoCode());
                x28JobAdvertisement.setSk5SchriftlichCode(languageSkill.getWrittenLevel().name());
                x28JobAdvertisement.setSk5MuendlichCode(languageSkill.getSpokenLevel().name());
            }
        }
    }

    private void mapEmployment(Employment employment, Oste x28JobAdvertisement) {
        if (employment != null) {
            x28JobAdvertisement.setAbSofort(employment.getImmediately());
            x28JobAdvertisement.setPensumVon(Long.valueOf(employment.getWorkloadPercentageMin()));
            x28JobAdvertisement.setPensumBis(Long.valueOf(employment.getWorkloadPercentageMax()));
        }
    }

    private void mapPublication(Publication publication, Oste x28JobAdvertisement) {
        if (publication != null) {
            x28JobAdvertisement.setAnmeldeDatum(DATE_FORMATTER.format(publication.getStartDate().atStartOfDay()));
            x28JobAdvertisement.setGueltigkeit(DATE_FORMATTER.format(publication.getEndDate().atStartOfDay().plusDays(1).minus(1, ChronoUnit.SECONDS)));
            x28JobAdvertisement.setEuresAnonym(publication.isEuresAnonymous());
            x28JobAdvertisement.setEuresPublikation(publication.isEures());
        }
    }

    private void mapLocation(Location location, Oste x28JobAdvertisement) {
        if (location != null) {
            x28JobAdvertisement.setArbeitsortAusland(location.getCountryIsoCode());
            x28JobAdvertisement.setArbeitsortGemeindeNr(location.getCommunalCode());
            x28JobAdvertisement.setArbeitsortKanton(location.getCantonCode());
            x28JobAdvertisement.setArbeitsortPlz(location.getPostalCode());
            x28JobAdvertisement.setArbeitsortRegion(location.getRegionCode());
            x28JobAdvertisement.setArbeitsortText(location.getRemarks());
        }
    }

    private void mapTitleAndDescription(List<JobDescription> jobDescriptions, Oste x28JobAdvertisement) {
        if (!isEmpty(jobDescriptions)) {
            JobDescription jobDescription = jobDescriptions.get(0);
            x28JobAdvertisement.setBezeichnung(jobDescription.getTitle());
            x28JobAdvertisement.setBeschreibung(jobDescription.getDescription());
        }
    }

    private void mapOccupation(List<Occupation> occupations, Oste x28JobAdvertisement) {
        if (!isEmpty(occupations)) {
            if (occupations.size() >= 1) {
                Occupation occupation = occupations.get(0);
                x28JobAdvertisement.setBq1AvamBerufNr(occupation.getAvamOccupationCode());
                x28JobAdvertisement.setBq1AusbildungCode(occupation.getEducationCode());
                x28JobAdvertisement.setBq1ErfahrungCode(occupation.getWorkExperience().toString());
            }

            if (occupations.size() >= 2) {
                Occupation occupation = occupations.get(1);
                x28JobAdvertisement.setBq2AvamBerufNr(occupation.getAvamOccupationCode());
                x28JobAdvertisement.setBq2AusbildungCode(occupation.getEducationCode());
                x28JobAdvertisement.setBq2ErfahrungCode(occupation.getWorkExperience().toString());
            }

            if (occupations.size() >= 3) {
                Occupation occupation = occupations.get(1);
                x28JobAdvertisement.setBq3AvamBerufNr(occupation.getAvamOccupationCode());
                x28JobAdvertisement.setBq3AusbildungCode(occupation.getEducationCode());
                x28JobAdvertisement.setBq3ErfahrungCode(occupation.getWorkExperience().toString());
            }
        }
    }

    private void mapPublicContact(PublicContact publicContact, Oste x28JobAdvertisement) {
        if (publicContact != null) {
            x28JobAdvertisement.setKpAnredeCode(publicContact.getSalutation().toString());
            x28JobAdvertisement.setKpName(publicContact.getLastName());
            x28JobAdvertisement.setKpVorname(publicContact.getFirstName());
            x28JobAdvertisement.setKpEMail(publicContact.getEmail());
            x28JobAdvertisement.setKpTelefonNr(publicContact.getPhone());
        }
    }
}
