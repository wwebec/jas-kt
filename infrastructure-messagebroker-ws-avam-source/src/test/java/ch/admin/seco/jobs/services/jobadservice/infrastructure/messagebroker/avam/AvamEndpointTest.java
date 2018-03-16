package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementEvents.JOB_ADVERTISEMENT_APPROVED;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageHeaders.EVENT;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.ws.test.server.ResponseMatchers;

import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.ApproveJobAdvertisementMessage;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AvamSourceApplication.class)
public class AvamEndpointTest {

    private MockWebServiceClient mockWebServiceClient;

    private JacksonTester<ApproveJobAdvertisementMessage> json;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MessageCollector messageCollector;

    @Autowired
    private Source source;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("classpath:/schema/SecoEgovService.xsd")
    private Resource secoEgovServiceXsdResource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockWebServiceClient = MockWebServiceClient.createClient(applicationContext);
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    public void shouldApproveJobAdvertisement() throws IOException {

        // process
        mockWebServiceClient.sendRequest(withPayload(getAsResource("soap/messages/insertOste-approved-1.xml")))
                .andExpect(ResponseMatchers.noFault())
                .andExpect(ResponseMatchers.validPayload(secoEgovServiceXsdResource));

        // assert
        Message<String> received = (Message<String>) messageCollector.forChannel(source.output()).poll();
        assertThat(received).isNotNull();
        assertThat(received.getHeaders().get(EVENT)).isEqualTo(JOB_ADVERTISEMENT_APPROVED);
        assertThat(json.parse(received.getPayload()).getObject().getStellennummerEgov()).isEqualTo("OI1349349");

    }

    private ClassPathResource getAsResource(String payloadFile) {
        return new ClassPathResource(payloadFile);
    }
}
