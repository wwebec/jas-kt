package ch.admin.seco.jobs.services.jobadservice.integration.x28.exporter.config;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.*;
import ch.admin.seco.jobs.services.jobadservice.integration.x28.jobadexport.Oste;
import org.springframework.batch.item.ItemProcessor;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.util.CollectionUtils.isEmpty;

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
        x28JobAdvertisement.setGleicheOste(jobContent.getNumberOfJobs());

        mapTitleAndDescription(jobContent.getJobDescriptions(), x28JobAdvertisement);
        mapOccupation(jobContent.getOccupations(), x28JobAdvertisement);
        mapLocation(jobContent.getLocation(), x28JobAdvertisement);
        mapPublication(jobAdvertisement.getPublication(), x28JobAdvertisement);
        mapEmployment(jobContent.getEmployment(), x28JobAdvertisement);
        mapPublicContact(jobContent.getPublicContact(), x28JobAdvertisement);
        mapLanguageSkills(jobContent.getLanguageSkills(), x28JobAdvertisement);
        mapCompany(jobContent.getDisplayCompany(), x28JobAdvertisement);
        mapApplyChannel(jobContent.getApplyChannel(), x28JobAdvertisement);

        return x28JobAdvertisement;
    }

    private void mapApplyChannel(ApplyChannel applyChannel, Oste x28JobAdvertisement) {
        if (applyChannel != null) {
            x28JobAdvertisement.setBewerbungSchriftlich(isNotBlank(applyChannel.getMailAddress()));

            x28JobAdvertisement.setBewerbungElektronisch(isNotBlank(applyChannel.getFormUrl()) || isNotBlank(applyChannel.getEmailAddress()));
            x28JobAdvertisement.setUntUrl(applyChannel.getFormUrl());
            x28JobAdvertisement.setUntEMail(applyChannel.getEmailAddress());

            x28JobAdvertisement.setBewerbungTelefonisch(isNotBlank(applyChannel.getPhoneNumber()));
            x28JobAdvertisement.setUntTelefon(applyChannel.getPhoneNumber());
        }
    }

    private void mapCompany(Company company, Oste x28JobAdvertisement) {
        x28JobAdvertisement.setUntName(company.getName());

        x28JobAdvertisement.setUntLand(company.getCountryIsoCode());
        x28JobAdvertisement.setUntPlz(company.getPostalCode());
        x28JobAdvertisement.setUntOrt(company.getCity());
        x28JobAdvertisement.setUntStrasse(company.getStreet());
        x28JobAdvertisement.setUntHausNr(company.getHouseNumber());

        x28JobAdvertisement.setUntPostfach(company.getPostOfficeBoxNumber());
        x28JobAdvertisement.setUntPostfachPlz(company.getPostOfficeBoxPostalCode());
        x28JobAdvertisement.setUntPostfachOrt(company.getPostOfficeBoxCity());
    }

    private void mapLanguageSkills(List<LanguageSkill> languageSkills, Oste x28JobAdvertisement) {
        if (!isEmpty(languageSkills)) {
            if (languageSkills.size() >= 1) {
                LanguageSkill languageSkill = languageSkills.get(0);
                x28JobAdvertisement.setSk1SpracheCode(languageSkill.getLanguageIsoCode());
                x28JobAdvertisement.setSk1SchriftlichCode(safeString(languageSkill.getWrittenLevel()));
                x28JobAdvertisement.setSk1MuendlichCode(safeString(languageSkill.getSpokenLevel()));
            }
            if (languageSkills.size() >= 2) {
                LanguageSkill languageSkill = languageSkills.get(1);
                x28JobAdvertisement.setSk2SpracheCode(languageSkill.getLanguageIsoCode());
                x28JobAdvertisement.setSk2SchriftlichCode(safeString(languageSkill.getWrittenLevel()));
                x28JobAdvertisement.setSk2MuendlichCode(safeString(languageSkill.getSpokenLevel()));
            }
            if (languageSkills.size() >= 3) {
                LanguageSkill languageSkill = languageSkills.get(2);
                x28JobAdvertisement.setSk3SpracheCode(languageSkill.getLanguageIsoCode());
                x28JobAdvertisement.setSk3SchriftlichCode(safeString(languageSkill.getWrittenLevel()));
                x28JobAdvertisement.setSk3MuendlichCode(safeString(languageSkill.getSpokenLevel()));
            }
            if (languageSkills.size() >= 4) {
                LanguageSkill languageSkill = languageSkills.get(3);
                x28JobAdvertisement.setSk4SpracheCode(languageSkill.getLanguageIsoCode());
                x28JobAdvertisement.setSk4SchriftlichCode(safeString(languageSkill.getWrittenLevel()));
                x28JobAdvertisement.setSk4MuendlichCode(safeString(languageSkill.getSpokenLevel()));
            }
            if (languageSkills.size() >= 5) {
                LanguageSkill languageSkill = languageSkills.get(4);
                x28JobAdvertisement.setSk5SpracheCode(languageSkill.getLanguageIsoCode());
                x28JobAdvertisement.setSk5SchriftlichCode(safeString(languageSkill.getWrittenLevel()));
                x28JobAdvertisement.setSk5MuendlichCode(safeString(languageSkill.getSpokenLevel()));
            }
        }
    }

    private void mapEmployment(Employment employment, Oste x28JobAdvertisement) {
        if (employment != null) {
            x28JobAdvertisement.setAbSofort(employment.isImmediately());
            x28JobAdvertisement.setUnbefristet(employment.isPermanent());

            if (employment.getStartDate() != null) {
                x28JobAdvertisement.setStellenantritt(DATE_FORMATTER.format(employment.getStartDate().atStartOfDay()));
            }

            if (employment.getEndDate() != null) {
                x28JobAdvertisement.setVertragsdauer(DATE_FORMATTER.format(employment.getEndDate().atStartOfDay().plusDays(1).minus(1, ChronoUnit.SECONDS)));
            }
            x28JobAdvertisement.setPensumVon((long) employment.getWorkloadPercentageMin());
            x28JobAdvertisement.setPensumBis((long) employment.getWorkloadPercentageMax());
        }
    }

    private void mapPublication(Publication publication, Oste x28JobAdvertisement) {
        if (publication != null) {

            if (publication.getStartDate() != null) {
                x28JobAdvertisement.setAnmeldeDatum(DATE_FORMATTER.format(publication.getStartDate().atStartOfDay()));
            }

            if (publication.getEndDate() != null) {
                x28JobAdvertisement.setGueltigkeit(DATE_FORMATTER.format(publication.getEndDate().atStartOfDay().plusDays(1).minus(1, ChronoUnit.SECONDS)));
            }

            x28JobAdvertisement.setEuresAnonym(publication.isEuresAnonymous());
            x28JobAdvertisement.setEuresPublikation(publication.isEuresDisplay());
            x28JobAdvertisement.setWwwAnonym(publication.isCompanyAnonymous());
        }
    }

    private void mapLocation(Location location, Oste x28JobAdvertisement) {
        if (location != null) {
            x28JobAdvertisement.setArbeitsortAusland(location.getCountryIsoCode());
            x28JobAdvertisement.setArbeitsortGemeindeNr(location.getCommunalCode());
            x28JobAdvertisement.setArbeitsortKanton(location.getCantonCode());
            x28JobAdvertisement.setArbeitsortPlz(location.getPostalCode());
            x28JobAdvertisement.setArbeitsortRegion(location.getRegionCode());
            x28JobAdvertisement.setArbeitsortText(location.getCity());
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
                x28JobAdvertisement.setBq1ErfahrungCode(safeString(occupation.getWorkExperience()));
            }

            if (occupations.size() >= 2) {
                Occupation occupation = occupations.get(1);
                x28JobAdvertisement.setBq2AvamBerufNr(occupation.getAvamOccupationCode());
                x28JobAdvertisement.setBq2AusbildungCode(occupation.getEducationCode());
                x28JobAdvertisement.setBq2ErfahrungCode(safeString(occupation.getWorkExperience()));
            }

            if (occupations.size() >= 3) {
                Occupation occupation = occupations.get(1);
                x28JobAdvertisement.setBq3AvamBerufNr(occupation.getAvamOccupationCode());
                x28JobAdvertisement.setBq3AusbildungCode(occupation.getEducationCode());
                x28JobAdvertisement.setBq3ErfahrungCode(safeString(occupation.getWorkExperience()));
            }
        }
    }

    private void mapPublicContact(PublicContact publicContact, Oste x28JobAdvertisement) {
        if (publicContact != null) {
            x28JobAdvertisement.setKpAnredeCode(safeString(publicContact.getSalutation()));
            x28JobAdvertisement.setKpName(publicContact.getLastName());
            x28JobAdvertisement.setKpVorname(publicContact.getFirstName());
            x28JobAdvertisement.setKpEMail(publicContact.getEmail());
            x28JobAdvertisement.setKpTelefonNr(publicContact.getPhone());
        }
    }

    private static String safeString(LanguageLevel languageLevel) {
        if (languageLevel != null) {
            return languageLevel.name();
        }

        return null;
    }

    private static String safeString(Object object) {
        if (object != null) {
            return object.toString();
        }

        return null;
    }
}
