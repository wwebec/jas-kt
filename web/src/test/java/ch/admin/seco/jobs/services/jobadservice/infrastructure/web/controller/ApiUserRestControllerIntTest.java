package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller;

import static ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUserTestFixture.createApiUser01;
import static ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUserTestFixture.createApiUser02;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ch.admin.seco.jobs.services.jobadservice.Application;
import ch.admin.seco.jobs.services.jobadservice.application.apiuser.ApiUserApplicationService;
import ch.admin.seco.jobs.services.jobadservice.application.apiuser.dto.CreateApiUserDto;
import ch.admin.seco.jobs.services.jobadservice.application.apiuser.dto.UpdateDetailsApiUserDto;
import ch.admin.seco.jobs.services.jobadservice.application.apiuser.dto.UpdatePasswordApiUserDto;
import ch.admin.seco.jobs.services.jobadservice.application.apiuser.dto.UpdateStatusApiUserDto;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.EventStore;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUser;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUserRepository;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.read.apiuser.ApiUserSearchRequest;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.read.apiuser.ApiUserSearchService;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.write.apiuser.ApiUserDocument;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.write.apiuser.ApiUserElasticsearchRepository;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.TestUtil;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller.errors.ExceptionTranslator;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("dev")
public class ApiUserRestControllerIntTest {

    private static final String API_API_USERS = "/api/apiUsers";

    private static final String JSON_PATH_ID = "$.id";
    private static final String JSON_PATH_USERNAME = "$.username";
    private static final String JSON_PATH_COMPANY_NAME = "$.companyName";
    private static final String JSON_PATH_COMPANY_EMAIL = "$.companyEmail";
    private static final String JSON_PATH_TECHNICAL_CONTACT_NAME = "$.technicalContactName";
    private static final String JSON_PATH_TECHNICAL_CONTACT_EMAIL = "$.technicalContactEmail";
    private static final String JSON_PATH_ACTIVE = "$.active";
    private static final String JSON_PATH_CREATE_DATE = "$.createDate";

    private static final String JSON_PATH_LIST_ID = "$.[0].id";
    private static final String JSON_PATH_LIST_USERNAME = "$.[0].username";
    private static final String JSON_PATH_LIST_COMPANY_NAME = "$.[0].companyName";
    private static final String JSON_PATH_LIST_COMPANY_EMAIL = "$.[0].companyEmail";
    private static final String JSON_PATH_LIST_TECHNICAL_CONTACT_NAME = "$.[0].technicalContactName";
    private static final String JSON_PATH_LIST_TECHNICAL_CONTACT_EMAIL = "$.[0].technicalContactEmail";
    private static final String JSON_PATH_LIST_ACTIVE = "$.[0].active";
    private static final String JSON_PATH_LIST_CREATE_DATE = "$.[0].createDate";

    @Autowired
    private ApiUserApplicationService apiUserApplicationService;

    @Autowired
    private ApiUserSearchService apiUserSearchService;

    @Autowired
    private ApiUserRepository apiUserRepository;

    @Autowired
    private EventStore eventStore;

    @Autowired
    private ApiUserElasticsearchRepository apiUserElasticsearchRepository;

    @Autowired
    private FormattingConversionService formattingConversionService;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        ApiUserRestController apiUserRestController =
                new ApiUserRestController(apiUserApplicationService, apiUserSearchService, eventStore);

        this.mockMvc = MockMvcBuilders.standaloneSetup(apiUserRestController)
                .setConversionService(formattingConversionService)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setControllerAdvice(exceptionTranslator)
                .setMessageConverters(jacksonMessageConverter)
                .build();
    }

    @After
    public void tearDown() {
        apiUserRepository.deleteAll();
        apiUserElasticsearchRepository.deleteAll();
    }

    @Test
    public void shouldCreateApiUser() throws Exception {
        // GIVEN
        ApiUser expectedApiUser = createApiUser01();
        CreateApiUserDto createApiUserDto = new CreateApiUserDto(
                expectedApiUser.getUsername(),
                expectedApiUser.getPassword(),
                expectedApiUser.getCompanyName(),
                expectedApiUser.getCompanyEmail(),
                expectedApiUser.getTechnicalContactName(),
                expectedApiUser.getTechnicalContactEmail(),
                expectedApiUser.isActive()
        );
        LocalDate expectedCreateDate = LocalDate.of(2018, 5, 10);
        TimeMachine.useFixedClockAt(expectedCreateDate.atStartOfDay());

        // WHEN
        ResultActions resultActions = mockMvc.perform(
                post(API_API_USERS)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(createApiUserDto))
        );

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath(JSON_PATH_USERNAME).value(expectedApiUser.getUsername()))
                .andExpect(jsonPath(JSON_PATH_COMPANY_NAME).value(expectedApiUser.getCompanyName()))
                .andExpect(jsonPath(JSON_PATH_COMPANY_EMAIL).value(expectedApiUser.getCompanyEmail()))
                .andExpect(jsonPath(JSON_PATH_TECHNICAL_CONTACT_NAME).value(expectedApiUser.getTechnicalContactName()))
                .andExpect(jsonPath(JSON_PATH_TECHNICAL_CONTACT_EMAIL).value(expectedApiUser.getTechnicalContactEmail()))
                .andExpect(jsonPath(JSON_PATH_ACTIVE).value(expectedApiUser.isActive()))
                .andExpect(jsonPath(JSON_PATH_CREATE_DATE).value(expectedCreateDate.format(DateTimeFormatter.ISO_LOCAL_DATE)));

        List<ApiUser> all = apiUserRepository.findAll();
        assertThat(all).hasSize(1);
        ApiUser apiUser = all.get(0);
        assertThat(apiUser.getUsername()).isEqualTo(expectedApiUser.getUsername());
        assertThat(apiUser.getCompanyName()).isEqualTo(expectedApiUser.getCompanyName());
        assertThat(apiUser.getCompanyEmail()).isEqualTo(expectedApiUser.getCompanyEmail());
        assertThat(apiUser.getTechnicalContactName()).isEqualTo(expectedApiUser.getTechnicalContactName());
        assertThat(apiUser.getTechnicalContactEmail()).isEqualTo(expectedApiUser.getTechnicalContactEmail());
        assertThat(apiUser.isActive()).isTrue();
        assertThat(apiUser.getCreateDate()).isEqualTo(expectedCreateDate);

        TimeMachine.reset();
    }

    @Test
	public void shouldNotSaveApiUserWithSameUsername() throws Exception {
		// GIVEN
		ApiUser originalApiUser = createApiUser01();
		storeDatabase(originalApiUser);
		// FIXME New Test

        CreateApiUserDto createApiUserDto = new CreateApiUserDto(
                originalApiUser.getUsername(),
                "password-new",
                "companyName-new",
                "companyEmail-new@example.com",
                "technicalContactName-new",
                "technicalContactEmail-new@example.com",
                true
        );

		// WHEN
		ResultActions resultActions = mockMvc.perform(
				post(API_API_USERS)
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(createApiUserDto))
		);

		// THEN
		resultActions
				.andExpect(status().is5xxServerError());

        List<ApiUser> all = apiUserRepository.findAll();
        assertThat(all).hasSize(1);
        ApiUser apiUser = all.get(0);
        assertThat(apiUser.getId()).isEqualTo(originalApiUser.getId());
	}

	@Test
    public void shouldFindById() throws Exception {
        // GIVEN
        ApiUser originalApiUser = createApiUser01();
        storeDatabase(originalApiUser);

        // WHEN
        ResultActions resultActions = mockMvc.perform(get(API_API_USERS + '/' + originalApiUser.getId().getValue()));

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath(JSON_PATH_ID).value(originalApiUser.getId().getValue()))
                .andExpect(jsonPath(JSON_PATH_USERNAME).value(originalApiUser.getUsername()))
                .andExpect(jsonPath(JSON_PATH_COMPANY_NAME).value(originalApiUser.getCompanyName()))
                .andExpect(jsonPath(JSON_PATH_COMPANY_EMAIL).value(originalApiUser.getCompanyEmail()))
                .andExpect(jsonPath(JSON_PATH_TECHNICAL_CONTACT_NAME).value(originalApiUser.getTechnicalContactName()))
                .andExpect(jsonPath(JSON_PATH_TECHNICAL_CONTACT_EMAIL).value(originalApiUser.getTechnicalContactEmail()))
                .andExpect(jsonPath(JSON_PATH_ACTIVE).value(originalApiUser.isActive()))
                .andExpect(jsonPath(JSON_PATH_CREATE_DATE).value(originalApiUser.getCreateDate().format(DateTimeFormatter.ISO_LOCAL_DATE)));
    }

    @Test
    public void shouldUpdateApiUserDetails() throws Exception {
        // GIVEN
        ApiUser originalApiUser = createApiUser01();
        storeDatabase(originalApiUser);

        UpdateDetailsApiUserDto updateDetailsApiUserDto = new UpdateDetailsApiUserDto(
                "username-new",
                "companyName-new",
                "companyEmail-new@example.com",
                "technicalContactName-new",
                "technicalContactEmail-new@example.com"
        );

        // WHEN
        ResultActions resultActions = mockMvc.perform(
                put(API_API_USERS + '/' + originalApiUser.getId().getValue())
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(updateDetailsApiUserDto))
        );

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath(JSON_PATH_ID).value(originalApiUser.getId().getValue()))
                .andExpect(jsonPath(JSON_PATH_USERNAME).value(updateDetailsApiUserDto.getUsername()))
                .andExpect(jsonPath(JSON_PATH_COMPANY_NAME).value(updateDetailsApiUserDto.getCompanyName()))
                .andExpect(jsonPath(JSON_PATH_COMPANY_EMAIL).value(updateDetailsApiUserDto.getCompanyEmail()))
                .andExpect(jsonPath(JSON_PATH_TECHNICAL_CONTACT_NAME).value(updateDetailsApiUserDto.getTechnicalContactName()))
                .andExpect(jsonPath(JSON_PATH_TECHNICAL_CONTACT_EMAIL).value(updateDetailsApiUserDto.getTechnicalContactEmail()))
                .andExpect(jsonPath(JSON_PATH_ACTIVE).value(originalApiUser.isActive()))
                .andExpect(jsonPath(JSON_PATH_CREATE_DATE).value(originalApiUser.getCreateDate().format(DateTimeFormatter.ISO_LOCAL_DATE)));

        ApiUser apiUser = apiUserRepository.findById(originalApiUser.getId()).get();
        assertThat(apiUser.getId().getValue()).isEqualTo(originalApiUser.getId().getValue());
        assertThat(apiUser.getUsername()).isEqualTo(updateDetailsApiUserDto.getUsername());
        assertThat(apiUser.getCompanyName()).isEqualTo(updateDetailsApiUserDto.getCompanyName());
        assertThat(apiUser.getCompanyEmail()).isEqualTo(updateDetailsApiUserDto.getCompanyEmail());
        assertThat(apiUser.isActive()).isEqualTo(originalApiUser.isActive());
        assertThat(apiUser.getTechnicalContactName()).isEqualTo(updateDetailsApiUserDto.getTechnicalContactName());
        assertThat(apiUser.getTechnicalContactEmail()).isEqualTo(updateDetailsApiUserDto.getTechnicalContactEmail());
        assertThat(apiUser.getCreateDate()).isEqualTo(originalApiUser.getCreateDate());
    }

    @Test
    public void shouldUpdateApiUserPassword() throws Exception {
        // GIVEN
        ApiUser originalApiUser = createApiUser01();
        storeDatabase(originalApiUser);

        UpdatePasswordApiUserDto updatePasswordApiUserDto = new UpdatePasswordApiUserDto(
                "password-new"
        );

        // WHEN
        mockMvc.perform(
                put(API_API_USERS + '/' + originalApiUser.getId().getValue() + "/password")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(updatePasswordApiUserDto))
        );

        // THEN
        ApiUser apiUser = apiUserRepository.findById(originalApiUser.getId()).get();
        assertThat(apiUser.getPassword()).isNotEqualTo(originalApiUser.getPassword());
    }

    @Test
    public void shouldUpdateApiUserStatus() throws Exception {
        // GIVEN
        ApiUser originalApiUser = createApiUser01();
        storeDatabase(originalApiUser);

        UpdateStatusApiUserDto updateStatusApiUserDto = new UpdateStatusApiUserDto(!originalApiUser.isActive());

        // WHEN
        mockMvc.perform(
                put(API_API_USERS + '/' + originalApiUser.getId().getValue() + "/active")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(updateStatusApiUserDto))
        );

        // THEN
        ApiUser apiUser = apiUserRepository.findById(originalApiUser.getId()).get();
        assertThat(apiUser.isActive()).isNotEqualTo(originalApiUser.isActive());
    }

    @Test
    public void shouldFindApiUser() throws Exception {
        // GIVEN
        ApiUser originalApiUser1 = createApiUser01();
        ApiUser originalApiUser2 = createApiUser02();
        storeElastic(originalApiUser1);
        storeElastic(originalApiUser2);

        ApiUserSearchRequest apiUserSearchRequest = new ApiUserSearchRequest(originalApiUser1.getUsername());

        // WHEN
        ResultActions resultActions = mockMvc.perform(
                post(API_API_USERS + "/_search")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(apiUserSearchRequest))
        );

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(header().string("X-Total-Count", "1"))
                .andExpect(jsonPath(JSON_PATH_LIST_ID).value(originalApiUser1.getId().getValue()))
                .andExpect(jsonPath(JSON_PATH_LIST_USERNAME).value(originalApiUser1.getUsername()))
                .andExpect(jsonPath(JSON_PATH_LIST_COMPANY_NAME).value(originalApiUser1.getCompanyName()))
                .andExpect(jsonPath(JSON_PATH_LIST_COMPANY_EMAIL).value(originalApiUser1.getCompanyEmail()))
                .andExpect(jsonPath(JSON_PATH_LIST_TECHNICAL_CONTACT_NAME).value(originalApiUser1.getTechnicalContactName()))
                .andExpect(jsonPath(JSON_PATH_LIST_TECHNICAL_CONTACT_EMAIL).value(originalApiUser1.getTechnicalContactEmail()))
                .andExpect(jsonPath(JSON_PATH_LIST_ACTIVE).value(originalApiUser1.isActive()))
                .andExpect(jsonPath(JSON_PATH_LIST_CREATE_DATE).value(originalApiUser1.getCreateDate().format(DateTimeFormatter.ISO_LOCAL_DATE)));
    }

    @Test
    public void shouldFindAllApiUsers() throws Exception {
        // GIVEN
        storeElastic(createApiUser01());
        storeElastic(createApiUser02());

        ApiUserSearchRequest apiUserSearchRequest = new ApiUserSearchRequest("");

        // WHEN
        ResultActions resultActions = mockMvc.perform(
                post(API_API_USERS + "/_search")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(apiUserSearchRequest))
        );

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(header().string("X-Total-Count", "2"));
    }

    private ApiUser storeDatabase(ApiUser apiUser) {
        return apiUserRepository.saveAndFlush(apiUser);
    }

    private void storeElastic(ApiUser apiUser) {
        apiUserElasticsearchRepository.save(new ApiUserDocument(apiUser));
    }

}