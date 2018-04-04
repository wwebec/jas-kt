package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.JobAdvertisementAction.APPROVE;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.JobAdvertisementAction.CANCEL;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.JobAdvertisementAction.CREATE_FROM_AVAM;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.JobAdvertisementAction.REJECT;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageHeaders.ACTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.ws.test.server.RequestCreators.withPayload;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.messaging.Message;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.ws.test.server.ResponseMatchers;

import ch.admin.seco.jobs.services.jobadservice.application.ProfileRegistry;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementFromAvamDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.ApprovalDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.CancellationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.RejectionDto;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AvamSourceApplication.class)
@ActiveProfiles(ProfileRegistry.AVAM_WSDL_V2)
@DirtiesContext
public class AvamEndpointV2Test {

    private MockWebServiceClient mockWebServiceClient;

    private JacksonTester<ApprovalDto> approvalDtoJacksonTester;
    private JacksonTester<RejectionDto> rejectionDtoJacksonTester;
    private JacksonTester<CreateJobAdvertisementFromAvamDto> createJobAdvertisementAvamDtoJacksonTester;
    private JacksonTester<CancellationDto> cancellationDtoJacksonTester;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MessageCollector messageCollector;

    @Autowired
    private Source source;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("classpath:/schema/v2/AVAMToEgov.xsd")
    private Resource secoEgovServiceXsdResource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockWebServiceClient = MockWebServiceClient.createClient(applicationContext);
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    public void approveJobAdvertisement() throws IOException {

        // process
        mockWebServiceClient.sendRequest(withPayload(getAsResource("soap/messages/insertOste-approve-1.xml")))
                .andExpect(ResponseMatchers.noFault())
                .andExpect(ResponseMatchers.validPayload(secoEgovServiceXsdResource));

        // assert
        Message<String> received = (Message<String>) messageCollector.forChannel(source.output()).poll();
        assertThat(received).isNotNull();
        assertThat(received.getHeaders().get(ACTION)).isEqualTo(APPROVE.name());
        ApprovalDto approvalDto = approvalDtoJacksonTester.parse(received.getPayload()).getObject();
        assertThat(approvalDto.getStellennummerEgov()).isEqualTo("EGOV-0001");
        assertThat(approvalDto.getStellennummerAvam()).isEqualTo("AVAM-0001");
        assertThat(approvalDto.getDate()).isEqualTo("2018-03-01");
    }

    @Test
    public void rejectJobAdvertisement() throws IOException {

        // process
        mockWebServiceClient.sendRequest(withPayload(getAsResource("soap/messages/insertOste-reject-1.xml")))
                .andExpect(ResponseMatchers.noFault())
                .andExpect(ResponseMatchers.validPayload(secoEgovServiceXsdResource));

        // assert
        Message<String> received = (Message<String>) messageCollector.forChannel(source.output()).poll();
        assertThat(received).isNotNull();
        assertThat(received.getHeaders().get(ACTION)).isEqualTo(REJECT.name());

        RejectionDto rejectionDto = rejectionDtoJacksonTester.parse(received.getPayload()).getObject();
        assertThat(rejectionDto.getStellennummerEgov()).isEqualTo("EGOV-0002");
        assertThat(rejectionDto.getStellennummerAvam()).isEqualTo("AVAM-0002");
        assertThat(rejectionDto.getDate()).isEqualTo("2018-03-03");
        assertThat(rejectionDto.getCode()).isEqualTo("REJECT-CODE");
        assertThat(rejectionDto.getReason()).isEqualTo("REJECT-REASON");
    }

    @Test
    public void createJobAdvertisement() throws IOException {

        // process
        mockWebServiceClient.sendRequest(withPayload(getAsResource("soap/messages/insertOste-create-1.xml")))
                .andExpect(ResponseMatchers.noFault())
                .andExpect(ResponseMatchers.validPayload(secoEgovServiceXsdResource));

        // assert
        Message<String> received = (Message<String>) messageCollector.forChannel(source.output()).poll();
        assertThat(received).isNotNull();
        assertThat(received.getHeaders().get(ACTION)).isEqualTo(CREATE_FROM_AVAM.name());

        CreateJobAdvertisementFromAvamDto createJobAdvertisementFromAvamDto = createJobAdvertisementAvamDtoJacksonTester.parse(received.getPayload()).getObject();
        assertThat(createJobAdvertisementFromAvamDto.getStellennummerAvam()).isEqualTo("AVAM-0003");
        assertThat(createJobAdvertisementFromAvamDto.getTitle()).isEqualTo("Test Title");
        assertThat(createJobAdvertisementFromAvamDto.getDescription()).isEqualTo("Test Description");
    }

    @Test
    public void cancelJobAdvertisement() throws IOException {

        // process
        mockWebServiceClient.sendRequest(withPayload(getAsResource("soap/messages/insertOste-cancel-1.xml")))
                .andExpect(ResponseMatchers.noFault())
                .andExpect(ResponseMatchers.validPayload(secoEgovServiceXsdResource));

        // assert
        Message<String> received = (Message<String>) messageCollector.forChannel(source.output()).poll();
        assertThat(received).isNotNull();
        assertThat(received.getHeaders().get(ACTION)).isEqualTo(CANCEL.name());

        CancellationDto cancellationDto = cancellationDtoJacksonTester.parse(received.getPayload()).getObject();
        assertThat(cancellationDto.getStellennummerEgov()).isEqualTo("EGOV-0004");
        assertThat(cancellationDto.getStellennummerAvam()).isEqualTo("AVAM-0004");
        assertThat(cancellationDto.getDate()).isEqualTo("2018-03-04");
        assertThat(cancellationDto.getCode()).isEqualTo("CANCEL-CODE");
    }


    private ClassPathResource getAsResource(String payloadFile) {
        return new ClassPathResource(payloadFile);
    }
}
