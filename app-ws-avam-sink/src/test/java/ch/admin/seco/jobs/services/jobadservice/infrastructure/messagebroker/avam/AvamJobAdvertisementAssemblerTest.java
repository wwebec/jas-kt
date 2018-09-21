package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.*;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.TOsteEgov;
import org.junit.Test;

import java.util.Collections;

import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.AvamJobAdvertisementAssembler.fetchFirstEmail;
import static org.assertj.core.api.Assertions.assertThat;

public class AvamJobAdvertisementAssemblerTest {

    @Test
    public void testFetchFirstEmail() {
        String inputEmails0 = null;
        String inputEmails1 = "";
        String inputEmails2 = "asdf.ghjk@example.org";
        String inputEmails3 = "asdf.ghjk@example.org, test@example.org";
        String inputEmails4 = "asdf.ghjk@example.org, test@example.org, fdsa@example.org";

        assertThat(fetchFirstEmail(inputEmails0)).isNull();
        assertThat(fetchFirstEmail(inputEmails1)).isNull();
        assertThat(fetchFirstEmail(inputEmails2)).isEqualTo("asdf.ghjk@example.org");
        assertThat(fetchFirstEmail(inputEmails3)).isEqualTo("asdf.ghjk@example.org");
        assertThat(fetchFirstEmail(inputEmails4)).isEqualTo("asdf.ghjk@example.org");
    }

    @Test
    public void shouldHaveCompanyAndEmployer() {
        Company company = new Company.Builder<>()
                .setName("companyName")
                .setStreet("companyStreet")
                .setPostalCode("companyPostalCode")
                .setCity("companyCity")
                .setCountryIsoCode("ch")
                .setSurrogate(true)
                .build();
        Employer employer = new Employer.Builder()
                .setName("employerName")
                .setPostalCode("employerPostalCode")
                .setCity("employerCity")
                .setCountryIsoCode("ch")
                .build();
        JobAdvertisement jobAdvertisement = createJobAdvertisementForCompanyAndEmployer(company, employer);

        AvamJobAdvertisementAssembler assembler = new AvamJobAdvertisementAssembler();

        TOsteEgov oste = assembler.toOsteEgov(jobAdvertisement, AvamAction.ANMELDUNG);

        assertThat(oste).isNotNull();

        assertThat(oste.getUntName()).isEqualTo(company.getName());
        assertThat(oste.getUntStrasse()).isEqualTo(company.getStreet());
        assertThat(oste.getUntHausNr()).isEqualTo(company.getHouseNumber());
        assertThat(oste.getUntPlz()).isEqualTo(company.getPostalCode());
        assertThat(oste.getUntOrt()).isEqualTo(company.getCity());
        assertThat(oste.getUntLand()).isEqualTo(company.getCountryIsoCode());
        assertThat(oste.isAuftraggeber()).isEqualTo(company.isSurrogate());

        assertThat(oste.getAuftraggeberName()).isEqualTo(employer.getName());
        assertThat(oste.getAuftraggeberPlz()).isEqualTo(employer.getPostalCode());
        assertThat(oste.getAuftraggeberOrt()).isEqualTo(employer.getCity());
        assertThat(oste.getAuftraggeberLand()).isEqualTo(employer.getCountryIsoCode());
    }

    @Test
    public void shouldHaveCompanyButNotEmployer() {
        Company company = new Company.Builder<>()
                .setName("companyName")
                .setStreet("companyStreet")
                .setPostalCode("companyPostalCode")
                .setCity("companyCity")
                .setCountryIsoCode("ch")
                .setSurrogate(false)
                .build();

        JobAdvertisement jobAdvertisement = createJobAdvertisementForCompanyAndEmployer(company, null);

        AvamJobAdvertisementAssembler assembler = new AvamJobAdvertisementAssembler();

        TOsteEgov oste = assembler.toOsteEgov(jobAdvertisement, AvamAction.ANMELDUNG);

        assertThat(oste).isNotNull();

        assertThat(oste.getUntName()).isEqualTo(company.getName());
        assertThat(oste.getUntStrasse()).isEqualTo(company.getStreet());
        assertThat(oste.getUntHausNr()).isEqualTo(company.getHouseNumber());
        assertThat(oste.getUntPlz()).isEqualTo(company.getPostalCode());
        assertThat(oste.getUntOrt()).isEqualTo(company.getCity());
        assertThat(oste.getUntLand()).isEqualTo(company.getCountryIsoCode());
        assertThat(oste.isAuftraggeber()).isEqualTo(company.isSurrogate());

        assertThat(oste.getAuftraggeberName()).isNull();
        assertThat(oste.getAuftraggeberPlz()).isNull();
        assertThat(oste.getAuftraggeberOrt()).isNull();
        assertThat(oste.getAuftraggeberLand()).isNull();
    }

    private JobAdvertisement createJobAdvertisementForCompanyAndEmployer(Company company, Employer employer) {
        Employment employment = new Employment.Builder()
                .setWorkloadPercentageMin(80)
                .setWorkloadPercentageMax(100)
                .build();
        Occupation occupation = new Occupation.Builder()
                .setAvamOccupationCode("avamOccupationCode")
                .build();
        LanguageSkill languageSkill = new LanguageSkill.Builder()
                .setLanguageIsoCode("de")
                .build();
        JobContent jobContent = new JobContent.Builder()
                .setTitle("title")
                .setDescription("description")
                .setCompany(company)
                .setEmployer(employer)
                .setEmployment(employment)
                .setOccupations(Collections.singletonList(occupation))
                .setLanguageSkills(Collections.singletonList(languageSkill))
                .build();
        Owner owner = new Owner.Builder()
                .setAccessToken("accessToken")
                .build();
        Publication publication = new Publication.Builder().build();
        return new JobAdvertisement.Builder()
                .setId(new JobAdvertisementId("id"))
                .setStatus(JobAdvertisementStatus.INSPECTING)
                .setSourceSystem(SourceSystem.JOBROOM)
                .setJobContent(jobContent)
                .setOwner(owner)
                .setPublication(publication)
                .build();
    }

}