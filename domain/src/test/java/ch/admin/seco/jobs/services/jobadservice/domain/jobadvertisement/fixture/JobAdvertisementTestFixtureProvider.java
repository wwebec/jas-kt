package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture;

import static java.util.Collections.singletonList;

import java.util.List;
import java.util.Locale;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.ApplyChannel;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Contact;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobDescription;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageLevel;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageSkill;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Location;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Occupation;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Owner;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.PublicContact;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Salutation;
import ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.JobCenter;
import ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.JobCenterAddress;

public class JobAdvertisementTestFixtureProvider {

    public static Contact testContact(JobAdvertisementId jobAdvertisementId) {
        return new Contact.Builder()
                .setLanguage(Locale.GERMAN)
                .setSalutation(Salutation.MR)
                .setEmail(String.format("mail-%s@mail.com", jobAdvertisementId.getValue()))
                .setPhone(String.format("+41 %s", jobAdvertisementId.getValue()))
                .setFirstName(String.format("first-name-%s", jobAdvertisementId.getValue()))
                .setLastName(String.format("last-name-%s", jobAdvertisementId.getValue()))
                .build();
    }

    public static Owner testOwner(JobAdvertisementId jobAdvertisementId) {
        return new Owner.Builder()
                .setAccessToken(String.format("access-token-%s", jobAdvertisementId.getValue()))
                .setCompanyId(String.format("avg-id-%s", jobAdvertisementId.getValue()))
                .setUserId(String.format("user-id-%s", jobAdvertisementId.getValue()))
                .build();
    }

    public static Occupation testOccupation() {
        return new Occupation.Builder()
                .setAvamOccupationCode("avamOccupationCode")
                .build();
    }

    public static Location testLocation() {
        return new Location.Builder()
                .setRemarks("remarks")
                .setCity("city")
                .setPostalCode("postalCode")
                .setCommunalCode(null)
                .setRegionCode(null)
                .setCantonCode("BE")
                .setCountryIsoCode("CH")
                .build();
    }

    public static LanguageSkill testLanguageSkill() {
        return new LanguageSkill.Builder()
                .setLanguageIsoCode("de")
                .setSpokenLevel(LanguageLevel.PROFICIENT)
                .setWrittenLevel(LanguageLevel.INTERMEDIATE)
                .build();
    }

    public static PublicContact createPublicContact(JobAdvertisementId jobAdvertisementId) {
        return new PublicContact.Builder()
                .setSalutation(Salutation.MR)
                .setEmail(String.format("mail-%s@mail.com", jobAdvertisementId.getValue()))
                .setPhone(String.format("+41 %s", jobAdvertisementId.getValue()))
                .setFirstName(String.format("first-name-%s", jobAdvertisementId.getValue()))
                .setLastName(String.format("last-name-%s", jobAdvertisementId.getValue()))
                .build();
    }

    public static List<LanguageSkill> createLanguageSkills() {
        return singletonList(new LanguageSkill.Builder().setLanguageIsoCode("de").build());
    }

    public static List<JobDescription> createJobDescriptions() {
        return singletonList(new JobDescription.Builder()
                .setTitle("Test TITLE")
                .setDescription("TEST JOB DESC")
                .setLanguage(Locale.GERMAN)
                .build());
    }

    public static ApplyChannel testApplyChannel() {
        return new ApplyChannel.Builder()
                .setAdditionalInfo("additionalInfo")
                .setMailAddress("mailAddress")
                .setFormUrl("formUrl")
                .setEmailAddress("emailAddress")
                .setPhoneNumber("phoneNumber")
                .build();
    }

    public static Owner testOwnerWithCompanyId(JobAdvertisementId jobAdvertisementId, String companyId) {
        return new Owner.Builder()
                .setAccessToken(String.format("access-token-%s", jobAdvertisementId.getValue()))
                .setCompanyId(companyId)
                .setUserId(String.format("user-id-%s", jobAdvertisementId.getValue()))
                .build();
    }
}
