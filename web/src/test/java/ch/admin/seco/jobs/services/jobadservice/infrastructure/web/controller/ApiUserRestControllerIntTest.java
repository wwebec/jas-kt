package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller;

import ch.admin.seco.jobs.services.jobadservice.Application;
import ch.admin.seco.jobs.services.jobadservice.application.apiuser.ApiUserApplicationService;
import ch.admin.seco.jobs.services.jobadservice.application.apiuser.dto.ChangeApiUserStatusDto;
import ch.admin.seco.jobs.services.jobadservice.application.apiuser.dto.CreateApiUserDto;
import ch.admin.seco.jobs.services.jobadservice.application.apiuser.dto.UpdateApiUserDto;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUser;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUserRepository;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUserTestDataProvider;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.read.apiuser.ApiUserSearchRequest;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.read.apiuser.ApiUserSearchService;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.write.apiuser.ApiUserDocument;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.write.apiuser.ApiUserElasticsearchRepository;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.TestUtil;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller.errors.ExceptionTranslator;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
                new ApiUserRestController(apiUserApplicationService, apiUserSearchService);

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
    public void shouldSaveApiUser() throws Exception {
        // GIVEN
        ApiUser expectedApiUser = ApiUserTestDataProvider.createApiUser01();
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
    public void shouldUpdateApiUser() throws Exception {
        // GIVEN
        ApiUser originalApiUser = ApiUserTestDataProvider.createApiUser01();
        storeDatabase(originalApiUser);

        UpdateApiUserDto updateApiUserDto = new UpdateApiUserDto(
                originalApiUser.getId().getValue(),
                "username-new",
                "password-new",
                "companyName-new",
                "companyEmail-new@example.com",
                "technicalContactName-new",
                "technicalContactEmail-new@example.com",
                false
        );

        // WHEN
        ResultActions resultActions = mockMvc.perform(
                put(API_API_USERS)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(updateApiUserDto))
        );

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath(JSON_PATH_ID).value(updateApiUserDto.getId()))
                .andExpect(jsonPath(JSON_PATH_USERNAME).value(updateApiUserDto.getUsername()))
                .andExpect(jsonPath(JSON_PATH_COMPANY_NAME).value(updateApiUserDto.getCompanyName()))
                .andExpect(jsonPath(JSON_PATH_COMPANY_EMAIL).value(updateApiUserDto.getCompanyEmail()))
                .andExpect(jsonPath(JSON_PATH_TECHNICAL_CONTACT_NAME).value(updateApiUserDto.getTechnicalContactName()))
                .andExpect(jsonPath(JSON_PATH_TECHNICAL_CONTACT_EMAIL).value(updateApiUserDto.getTechnicalContactEmail()))
                .andExpect(jsonPath(JSON_PATH_ACTIVE).value(updateApiUserDto.isActive()))
                .andExpect(jsonPath(JSON_PATH_CREATE_DATE).value(originalApiUser.getCreateDate().format(DateTimeFormatter.ISO_LOCAL_DATE)));

        List<ApiUser> all = apiUserRepository.findAll();
        assertThat(all).hasSize(1);
        ApiUser apiUser = all.get(0);
        assertThat(apiUser.getId().getValue()).isEqualTo(originalApiUser.getId().getValue());
        assertThat(apiUser.getUsername()).isEqualTo(updateApiUserDto.getUsername());
        assertThat(apiUser.getCompanyName()).isEqualTo(updateApiUserDto.getCompanyName());
        assertThat(apiUser.getCompanyEmail()).isEqualTo(updateApiUserDto.getCompanyEmail());
        assertThat(apiUser.isActive()).isFalse();
        assertThat(apiUser.getTechnicalContactName()).isEqualTo(updateApiUserDto.getTechnicalContactName());
        assertThat(apiUser.getTechnicalContactEmail()).isEqualTo(updateApiUserDto.getTechnicalContactEmail());
        assertThat(apiUser.getCreateDate()).isEqualTo(originalApiUser.getCreateDate());
    }

    @Test
    public void shouldFindById() throws Exception {
        // GIVEN
        ApiUser originalApiUser = ApiUserTestDataProvider.createApiUser01();
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
    public void shouldChangeApiUserStatus() throws Exception {
        // GIVEN
        ApiUser originalApiUser = ApiUserTestDataProvider.createApiUser01();
        storeDatabase(originalApiUser);

        ChangeApiUserStatusDto changeApiUserStatusDto = new ChangeApiUserStatusDto(!originalApiUser.isActive());

        // WHEN
        mockMvc.perform(
                put(API_API_USERS + '/' + originalApiUser.getId().getValue() + "/active")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(changeApiUserStatusDto))
        );

        // THEN
        ApiUser apiUser = apiUserRepository.findById(originalApiUser.getId()).get();
        assertThat(apiUser.isActive()).isNotEqualTo(originalApiUser.isActive());
    }

    @Test
    public void shouldFindApiUser() throws Exception {
        // GIVEN
        ApiUser originalApiUser1 = ApiUserTestDataProvider.createApiUser01();
        ApiUser originalApiUser2 = ApiUserTestDataProvider.createApiUser02();
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
        storeElastic(ApiUserTestDataProvider.createApiUser01());
        storeElastic(ApiUserTestDataProvider.createApiUser02());

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