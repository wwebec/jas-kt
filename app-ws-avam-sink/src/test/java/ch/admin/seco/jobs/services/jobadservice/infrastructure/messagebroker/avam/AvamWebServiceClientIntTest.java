package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.test.client.MockWebServiceServer;
import org.springframework.xml.transform.ResourceSource;

import javax.xml.transform.Source;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Locale;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.CancellationCode.OCCUPIED_JOBCENTER;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.INSPECTING;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem.JOBROOM;
import static org.springframework.ws.test.client.RequestMatchers.payload;
import static org.springframework.ws.test.client.ResponseCreators.withPayload;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AvamWebServiceSinkApplication.class)
@TestPropertySource(properties = {
        "jobroom.ws.avam.sink.endPointUrl=test",
        "jobroom.ws.avam.sink.password=test",
        "jobroom.ws.avam.sink.username=test"
})
public class AvamWebServiceClientIntTest {
    private MockWebServiceServer mockServer;

    @Autowired
    private WebServiceTemplate webServiceTemplate;

    @Autowired
    private AvamWebServiceClient sut;

    @Autowired
    private ResourceLoader resourceLoader;

    @Before
    public void setUp() {
        this.mockServer = MockWebServiceServer.createServer(webServiceTemplate);
    }

    @Test
    public void shouldEscapeNonXMLValues() throws Exception {
        Source responsePayload = new ResourceSource(resourceLoader.getResource("classpath:ok-response.xml"));
        Source expectedRequestPayload = new ResourceSource(resourceLoader.getResource("classpath:job-ad-request.xml"));

        mockServer.expect(payload(expectedRequestPayload)).andRespond(withPayload(responsePayload));

        JobAdvertisement jobAdvertisement = createJobAdvertisement("test-\u001Fdescription \u0026 more");
        sut.register(jobAdvertisement);
    }

    private JobAdvertisement createJobAdvertisement(String description) {
        return new JobAdvertisement.Builder()
                .setId(new JobAdvertisementId("ID1"))
                .setStatus(INSPECTING)
                .setSourceSystem(JOBROOM)
                .setExternalReference("externalReference")
                .setStellennummerEgov("100")
                .setStellennummerAvam("stellennummerAvam")
                .setFingerprint("fingerprint")
                .setReportingObligationEndDate(LocalDate.of(2018, 9, 30).plusWeeks(4))
                .setJobCenterCode("jobCenterCode")
                .setApprovalDate(LocalDate.of(2018, 9, 30).plusWeeks(3))
                .setRejectionDate(LocalDate.of(2018, 9, 30).plusWeeks(2))
                .setRejectionCode("rejectionCode")
                .setRejectionReason("rejectionReason")
                .setCancellationDate(LocalDate.of(2018, 9, 30).plusWeeks(1))
                .setCancellationCode(OCCUPIED_JOBCENTER)
                .setJobContent(new JobContent.Builder()
                        .setJobDescriptions(Collections.singletonList(new JobDescription.Builder()
                                .setLanguage(Locale.GERMAN)
                                .setTitle("title")
                                .setDescription(description)
                                .build()))
                        .setCompany(new Company.Builder<>()
                                .setName("companyName")
                                .setStreet("companyStreet")
                                .setPostalCode("companyPostalCode")
                                .setCity("companyCity")
                                .setCountryIsoCode("ch")
                                .setSurrogate(false)
                                .build())
                        .setEmployer(null)
                        .setEmployment(new Employment.Builder()
                                .setWorkloadPercentageMin(80)
                                .setWorkloadPercentageMax(100)
                                .build())
                        .setOccupations(Collections.singletonList(new Occupation.Builder()
                                .setAvamOccupationCode("avamOccupationCode")
                                .build()))
                        .setLanguageSkills(Collections.singletonList(new LanguageSkill.Builder()
                                .setLanguageIsoCode("de")
                                .build()))
                        .build())
                .setOwner(new Owner.Builder()
                        .setAccessToken("accessToken")
                        .build())
                .setPublication(new Publication.Builder().build())
                .build();
    }
}