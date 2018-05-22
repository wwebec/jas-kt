package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller;

import ch.admin.seco.jobs.services.jobadservice.Application;
import ch.admin.seco.jobs.services.jobadservice.application.apiuser.ApiUserService;
import ch.admin.seco.jobs.services.jobadservice.application.apiuser.dto.ChangeApiUserStatusDto;
import ch.admin.seco.jobs.services.jobadservice.application.apiuser.dto.CreateApiUserDto;
import ch.admin.seco.jobs.services.jobadservice.application.apiuser.dto.UpdateApiUserDto;
import ch.admin.seco.jobs.services.jobadservice.application.security.CurrentUserContext;
import ch.admin.seco.jobs.services.jobadservice.application.security.TestingCurrentUserContext;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUser;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUserId;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUserRepository;
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
import org.springframework.context.annotation.Bean;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("dev")
public class ApiUserRestControllerIntTest {
    private static final String API_API_USERS = "/api/apiUsers";

    @Autowired
    private ApiUserService apiUserService;
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

    private ApiUser apiUser1;
    private ApiUser apiUser2;

    @Bean
    public CurrentUserContext currentUserContext() {
        return new TestingCurrentUserContext("junit");
    }

    @Before
    public void setUp() {
        ApiUserRestController apiUserRestController =
                new ApiUserRestController(apiUserService, apiUserSearchService);

        this.mockMvc = MockMvcBuilders.standaloneSetup(apiUserRestController)
                .setConversionService(formattingConversionService)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setControllerAdvice(exceptionTranslator)
                .setMessageConverters(jacksonMessageConverter)
                .build();

        apiUser1 = createApiUser(
                "id1",
                "alex",
                "secret1",
                true,
                "Company1",
                "email@email1",
                "contactName1",
                "contact@mail.com1",
                LocalDate.of(2018, 5, 10));

        apiUser2 = createApiUser(
                "id2",
                "bob",
                "secre2",
                true,
                "Company2",
                "email@email2",
                "contactName2",
                "contact@mail.com2",
                LocalDate.of(2018, 6, 22));
    }

    @After
    public void tearDown() {
        apiUserRepository.deleteAll();
        apiUserElasticsearchRepository.deleteAll();
    }

    @Test
    public void shouldSaveApiUser() throws Exception {
        // GIVEN
        CreateApiUserDto createApiUserDto = new CreateApiUserDto();
        createApiUserDto.setUsername("user");
        createApiUserDto.setPassword("secret");
        createApiUserDto.setCompanyName("Company");
        createApiUserDto.setEmail("email@email");
        createApiUserDto.setActive(true);
        createApiUserDto.setContactName("contactName");
        createApiUserDto.setContactEmail("contact@mail.com");
        TimeMachine.useFixedClockAt(LocalDateTime.of(2018, 5, 10, 0, 0));

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

                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.companyName").value("Company"))
                .andExpect(jsonPath("$.email").value("email@email"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.contactName").value("contactName"))
                .andExpect(jsonPath("$.contactEmail").value("contact@mail.com"))
                .andExpect(jsonPath("$.createDate").value("2018-05-10"));

        List<ApiUser> all = apiUserRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getUsername()).isEqualTo("user");
        assertThat(all.get(0).getCompanyName()).isEqualTo("Company");
        assertThat(all.get(0).getEmail()).isEqualTo("email@email");
        assertThat(all.get(0).getActive()).isTrue();
        assertThat(all.get(0).getContactName()).isEqualTo("contactName");
        assertThat(all.get(0).getContactEmail()).isEqualTo("contact@mail.com");

        TimeMachine.reset();
    }

    @Test
    public void shouldUpdateApiUser() throws Exception {
        // GIVEN
        saveApiUser(apiUser1);

        UpdateApiUserDto updateApiUserDto = new UpdateApiUserDto();
        updateApiUserDto.setId("id1");
        updateApiUserDto.setUsername("user-new");
        updateApiUserDto.setPassword("secret-new");
        updateApiUserDto.setEmail("email@email-new");
        updateApiUserDto.setActive(false);
        updateApiUserDto.setCompanyName("Company-new");
        updateApiUserDto.setContactName("contactName-new");
        updateApiUserDto.setContactEmail("contact@mail.com-new");

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

                .andExpect(jsonPath("$.id").value("id1"))
                .andExpect(jsonPath("$.username").value("user-new"))
                .andExpect(jsonPath("$.companyName").value("Company-new"))
                .andExpect(jsonPath("$.email").value("email@email-new"))
                .andExpect(jsonPath("$.active").value(false))
                .andExpect(jsonPath("$.contactName").value("contactName-new"))
                .andExpect(jsonPath("$.contactEmail").value("contact@mail.com-new"))
                .andExpect(jsonPath("$.createDate").value("2018-05-10"));

        List<ApiUser> all = apiUserRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getId().getValue()).isEqualTo("id1");
        assertThat(all.get(0).getUsername()).isEqualTo("user-new");
        assertThat(all.get(0).getCompanyName()).isEqualTo("Company-new");
        assertThat(all.get(0).getEmail()).isEqualTo("email@email-new");
        assertThat(all.get(0).getActive()).isFalse();
        assertThat(all.get(0).getContactName()).isEqualTo("contactName-new");
        assertThat(all.get(0).getContactEmail()).isEqualTo("contact@mail.com-new");
    }

    @Test
    public void shouldFindById() throws Exception {
        // GIVEN
        ApiUser savedUser = saveApiUser(apiUser1);

        // WHEN
        ResultActions resultActions = mockMvc.perform(get(API_API_USERS + '/' + savedUser.getId().getValue()));

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

                .andExpect(jsonPath("$.username").value("alex"))
                .andExpect(jsonPath("$.companyName").value("Company1"))
                .andExpect(jsonPath("$.email").value("email@email1"))
                .andExpect(jsonPath("$.contactName").value("contactName1"))
                .andExpect(jsonPath("$.contactEmail").value("contact@mail.com1"))
                .andExpect(jsonPath("$.createDate").value("2018-05-10"));
    }

    @Test
    public void shouldChangeApiUserStatus() throws Exception {
        // GIVEN
        ApiUser savedUser = saveApiUser(apiUser1);
        ChangeApiUserStatusDto changeApiUserStatusDto = new ChangeApiUserStatusDto(false);

        // WHEN
        mockMvc.perform(
                put(API_API_USERS + '/' + savedUser.getId().getValue() + "/active")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(changeApiUserStatusDto))
        );

        // THEN
        ApiUser apiUser = apiUserRepository.findById(savedUser.getId()).get();
        assertThat(apiUser.getActive()).isFalse();
    }

    @Test
    public void shouldFindApiUser() throws Exception {
        // GIVEN
        index(apiUser1);
        index(apiUser2);

        ApiUserSearchRequest apiUserSearchRequest = new ApiUserSearchRequest("alex");

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

                .andExpect(jsonPath("$.[0].username").value("alex"))
                .andExpect(jsonPath("$.[0].companyName").value("Company1"))
                .andExpect(jsonPath("$.[0].email").value("email@email1"))
                .andExpect(jsonPath("$.[0].contactName").value("contactName1"))
                .andExpect(jsonPath("$.[0].contactEmail").value("contact@mail.com1"))
                .andExpect(jsonPath("$.[0].createDate").value("2018-05-10"));
    }

    @Test
    public void shouldFindAllApiUsers() throws Exception {
        // GIVEN
        index(apiUser1);
        index(apiUser2);

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

    private void index(ApiUser apiUser) {
        apiUserElasticsearchRepository.save(new ApiUserDocument(apiUser));
    }

    private ApiUser saveApiUser(ApiUser apiUser) {
        return apiUserRepository.saveAndFlush(apiUser);
    }

    private ApiUser createApiUser(String id, String username, String password, boolean active, String companyName, String email, String contactName, String contactEmail, LocalDate createDate) {
        ApiUser.Builder builder = new ApiUser.Builder();
        builder.setId(new ApiUserId(id));
        builder.setUsername(username);
        builder.setPassword(password);
        builder.setActive(active);
        builder.setCompanyName(companyName);
        builder.setEmail(email);
        builder.setContactName(contactName);
        builder.setContactEmail(contactEmail);
        builder.setCreateDate(createDate);
        return new ApiUser(builder);
    }
}