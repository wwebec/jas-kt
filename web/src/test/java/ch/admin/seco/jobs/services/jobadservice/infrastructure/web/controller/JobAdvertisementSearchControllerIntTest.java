package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller;

import ch.admin.seco.jobs.services.jobadservice.Application;
import ch.admin.seco.jobs.services.jobadservice.application.security.CurrentUserContext;
import ch.admin.seco.jobs.services.jobadservice.application.security.Role;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.*;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.ElasticsearchConfiguration;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.read.jobadvertisement.*;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.write.jobadvertisement.JobAdvertisementDocument;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.write.jobadvertisement.JobAdvertisementElasticsearchRepository;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.TestUtil;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller.errors.ExceptionTranslator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementTestDataProvider.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("dev")
public class JobAdvertisementSearchControllerIntTest {

    private static final String DEFAULT_AVAM_CODE = "11111";
    private static final String DEFAULT_BFS_CODE = "11111111";
    private static final String API_JOB_ADVERTISEMENTS = "/api/jobAdvertisements";

    @Autowired
    private JobAdvertisementRepository jobAdvertisementJpaRepository;

    @Autowired
    private JobAdvertisementElasticsearchRepository jobAdvertisementElasticsearchRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ElasticsearchConfiguration.CustomEntityMapper customEntityMapper;


    @Autowired
    private FormattingConversionService formattingConversionService;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private JobAdvertisementSearchService jobAdvertisementSearchService;

    private CurrentUserContext mockCurrentUserContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.jobAdvertisementElasticsearchRepository.deleteAll();
        this.jobAdvertisementJpaRepository.deleteAll();
        this.mockCurrentUserContext = mock(CurrentUserContext.class);

        this.jobAdvertisementSearchService = new JobAdvertisementSearchService(mockCurrentUserContext,
                this.elasticsearchTemplate,
                this.customEntityMapper,
                this.jobAdvertisementElasticsearchRepository
        );

        JobAdvertisementSearchController jobAdvertisementSearchController
                = new JobAdvertisementSearchController(jobAdvertisementSearchService);

        this.mockMvc = MockMvcBuilders.standaloneSetup(jobAdvertisementSearchController)
                .setConversionService(formattingConversionService)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setControllerAdvice(exceptionTranslator)
                .setMessageConverters(jacksonMessageConverter)
                .build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void shouldSearchWithoutQuery() throws Exception {
        // GIVEN
        index(createJob(JOB_ADVERTISEMENT_ID_01));
        index(createJob(JOB_ADVERTISEMENT_ID_02));
        index(createJob(JOB_ADVERTISEMENT_ID_03));

        // WHEN
        ResultActions resultActions = mockMvc.perform(
                post(API_JOB_ADVERTISEMENTS + "/_search")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(new JobAdvertisementSearchRequest()))
        );

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(header().string("X-Total-Count", "3"));
    }

    @Test
    public void shouldNotSearchPeaJobsWithoutQuery() throws Exception {
        // GIVEN
        index(createJob(JOB_ADVERTISEMENT_ID_01));
        index(createJob(JOB_ADVERTISEMENT_ID_02));
        index(createJob(JOB_ADVERTISEMENT_ID_03));

        // WHEN
        ResultActions resultActions = mockMvc.perform(
                post(API_JOB_ADVERTISEMENTS + "/_search/pea")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(new PeaJobAdvertisementSearchRequest()))
        );

        // THEN
        resultActions
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldSearchPeaJobsWithoutJobTitleInRequest() throws Exception {
        // GIVEN
        index(createJobWithDescriptionAndOwnerCompanyId(JOB_ADVERTISEMENT_ID_01, "c++ developer", "c++ & java entwickler", "company-1"));
        index(createJobWithDescriptionAndOwnerCompanyId(JOB_ADVERTISEMENT_ID_02, "java & javascript developer", "jee entwickler", "company-1"));
        index(createJobWithDescriptionAndOwnerCompanyId(JOB_ADVERTISEMENT_ID_03, "php programmierer", "php programierer", "company-2"));
        PeaJobAdvertisementSearchRequest request = new PeaJobAdvertisementSearchRequest();
        request.setCompanyId("company-1");

        // WHEN
        ResultActions resultActions = mockMvc.perform(
                post(API_JOB_ADVERTISEMENTS + "/_search/pea")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(request))
        );

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(header().string("X-Total-Count", "2"));
    }

    @Test
    public void shouldSearchPeaJobsByPublicationDate() throws Exception {
        // GIVEN
        index(createJobWithOwnerAndPublicationStartDate(JOB_ADVERTISEMENT_ID_01, "company-1", LocalDate.now().minusDays(30)));
        index(createJobWithOwnerAndPublicationStartDate(JOB_ADVERTISEMENT_ID_02, "company-1", LocalDate.now().minusDays(7)));
        index(createJobWithOwnerAndPublicationStartDate(JOB_ADVERTISEMENT_ID_03, "company-1", LocalDate.now()));
        index(createJob(JOB_ADVERTISEMENT_ID_04));
        PeaJobAdvertisementSearchRequest request = new PeaJobAdvertisementSearchRequest();
        request.setCompanyId("company-1");
        request.setOnlineSinceDays(7);

        // WHEN
        ResultActions resultActions = mockMvc.perform(
                post(API_JOB_ADVERTISEMENTS + "/_search/pea")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(request))
        );

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(header().string("X-Total-Count", "2"));
    }

    @Test
    public void shouldSearchPeaJobsByJobTitle() throws Exception {
        // GIVEN
        index(createJobWithDescriptionAndOwnerCompanyId(JOB_ADVERTISEMENT_ID_01, "c++ developer", "c++ & java entwickler", "company-1"));
        index(createJobWithDescriptionAndOwnerCompanyId(JOB_ADVERTISEMENT_ID_02, "java & javascript developer", "jee entwickler", "company-1"));
        index(createJobWithDescriptionAndOwnerCompanyId(JOB_ADVERTISEMENT_ID_03, "php programmierer", "php programierer", "company-2"));
        index(createJobWithDescriptionAndOwnerCompanyId(JOB_ADVERTISEMENT_ID_04, "javascript developer", "javascript developer", "company-3"));
        PeaJobAdvertisementSearchRequest request = new PeaJobAdvertisementSearchRequest();
        request.setCompanyId("company-1");
        request.setJobTitle("developer");

        // WHEN
        ResultActions resultActions = mockMvc.perform(
                post(API_JOB_ADVERTISEMENTS + "/_search/pea")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(request))
        );

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(header().string("X-Total-Count", "2"));
    }

    @Test
    public void shouldSearchByKeyword() throws Exception {
        // GIVEN
        index(createJobWithDescription(JOB_ADVERTISEMENT_ID_01, "c++ developer", "c++ & java entwickler"));
        index(createJobWithDescription(JOB_ADVERTISEMENT_ID_02, "java & javascript developer", "jee entwickler"));
        index(createJobWithDescription(JOB_ADVERTISEMENT_ID_03, "php programmierer", "php programierer"));

        // WHEN
        JobAdvertisementSearchRequest searchRequest = new JobAdvertisementSearchRequest();
        searchRequest.setKeywords(new String[]{"entwickler", "java"});

        ResultActions resultActions = mockMvc.perform(
                post(API_JOB_ADVERTISEMENTS + "/_search")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(searchRequest))
        );

        // THEN
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(header().string("X-Total-Count", "2"))

                .andExpect(jsonPath("$.[0].id").value(equalTo(JOB_ADVERTISEMENT_ID_01.getValue())))
                .andExpect(jsonPath("$.[0].jobContent.title").value(equalTo("c++ developer")))
                .andExpect(jsonPath("$.[0].jobContent.description").value(equalTo("c++ &amp; <em>java</em> <em>entwickler</em>")))

                .andExpect(jsonPath("$.[1].id").value(equalTo(JOB_ADVERTISEMENT_ID_02.getValue())))
                .andExpect(jsonPath("$.[1].jobContent.title").value(equalTo("<em>java</em> & <em>javascript</em> developer")))
                .andExpect(jsonPath("$.[1].jobContent.description").value(equalTo("jee <em>entwickler</em>")));
    }

    @Test
    public void shouldSearchBySourceSystemKeyword() throws Exception {
        // GIVEN
        index(createJobWithDescription(JOB_ADVERTISEMENT_ID_01, "c++ developer", "c++ & java entwickler", SourceSystem.EXTERN));
        index(createJobWithDescription(JOB_ADVERTISEMENT_ID_02, "java & javascript developer", "jee entwickler", SourceSystem.API));

        // WHEN
        JobAdvertisementSearchRequest searchRequest = new JobAdvertisementSearchRequest();
        searchRequest.setKeywords(new String[]{"*extern"});

        ResultActions resultActions = mockMvc.perform(
                post(API_JOB_ADVERTISEMENTS + "/_search")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(searchRequest))
        );

        // THEN
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(header().string("X-Total-Count", "1"))

                .andExpect(jsonPath("$.[0].id").value(equalTo(JOB_ADVERTISEMENT_ID_01.getValue())))
                .andExpect(jsonPath("$.[0].jobContent.title").value(equalTo("c++ developer")))
                .andExpect(jsonPath("$.[0].jobContent.description").value(equalTo("c++ &amp; java entwickler")));
    }

    @Test
    public void shouldSearchByLanguageSkillKeyword() throws Exception {
        // GIVEN
        final LanguageSkill da = new LanguageSkill.Builder().setLanguageIsoCode("da").setSpokenLevel(LanguageLevel.PROFICIENT).setWrittenLevel(LanguageLevel.INTERMEDIATE).build();
        final LanguageSkill en = new LanguageSkill.Builder().setLanguageIsoCode("en").setSpokenLevel(LanguageLevel.PROFICIENT).setWrittenLevel(LanguageLevel.INTERMEDIATE).build();

        index(createJobWithLanguageSkills(JOB_ADVERTISEMENT_ID_01, "c++ developer", "c++ & java entwickler", SourceSystem.EXTERN, da));
        index(createJobWithLanguageSkills(JOB_ADVERTISEMENT_ID_02, "java & javascript developer", "jee entwickler", SourceSystem.API, en));

        // WHEN
        JobAdvertisementSearchRequest searchRequest = new JobAdvertisementSearchRequest();
        searchRequest.setKeywords(new String[] {"dänisch"});

        ResultActions resultActions = mockMvc.perform(
                post(API_JOB_ADVERTISEMENTS + "/_search")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(searchRequest))
        );

        // THEN
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(header().string("X-Total-Count", "1"));
    }

    @Test
    public void shouldSearchByOccupation_BFS() throws Exception {

        // GIVEN
        Occupation occupation1 = new Occupation.Builder().setAvamOccupationCode(DEFAULT_AVAM_CODE).setBfsCode(DEFAULT_BFS_CODE).build();
        Occupation occupation2 = new Occupation.Builder().setAvamOccupationCode(DEFAULT_AVAM_CODE).build();
        Occupation occupation3 = new Occupation.Builder().setAvamOccupationCode(DEFAULT_AVAM_CODE).setBfsCode("dummy").build();
        Occupation occupation4 = new Occupation.Builder().setAvamOccupationCode(DEFAULT_AVAM_CODE).setBfsCode(DEFAULT_BFS_CODE).build();

        index(createJobWithOccupation(JOB_ADVERTISEMENT_ID_01, occupation1));
        index(createJobWithOccupation(JOB_ADVERTISEMENT_ID_02, occupation2));
        index(createJobWithOccupation(JOB_ADVERTISEMENT_ID_03, occupation3));
        index(createJobWithOccupation(JOB_ADVERTISEMENT_ID_04, occupation4));

        // WHEN
        JobAdvertisementSearchRequest searchRequest = new JobAdvertisementSearchRequest();
        searchRequest.setProfessionCodes(new ProfessionCode[]{new ProfessionCode(ProfessionCodeType.BFS, DEFAULT_BFS_CODE)});

        ResultActions resultActions = mockMvc.perform(
                post(API_JOB_ADVERTISEMENTS + "/_search")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(searchRequest))
        );

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(header().string("X-Total-Count", "2"))
                .andExpect(jsonPath("$.[*].id").value(hasItem(JOB_ADVERTISEMENT_ID_01.getValue())))
                .andExpect(jsonPath("$.[*].id").value(hasItem(JOB_ADVERTISEMENT_ID_04.getValue())))
        ;
    }

    @Test
    public void shouldSearchByOccupation_X28() throws Exception {

        // GIVEN
        index(createJobWithX28Code(JOB_ADVERTISEMENT_ID_01, "1111,2222"));
        index(createJobWithX28Code(JOB_ADVERTISEMENT_ID_02, "1111"));
        index(createJobWithX28Code(JOB_ADVERTISEMENT_ID_03, "3333"));
        index(createJobWithX28Code(JOB_ADVERTISEMENT_ID_04, "4444"));

        // WHEN
        JobAdvertisementSearchRequest searchRequest = new JobAdvertisementSearchRequest();
        searchRequest.setProfessionCodes(new ProfessionCode[]{
                new ProfessionCode(ProfessionCodeType.X28, "1111"),
                new ProfessionCode(ProfessionCodeType.X28, "44")
        });

        ResultActions resultActions = mockMvc.perform(
                post(API_JOB_ADVERTISEMENTS + "/_search")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(searchRequest))
        );

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(header().string("X-Total-Count", "2"))
                .andExpect(jsonPath("$.[*].id").value(hasItem(JOB_ADVERTISEMENT_ID_01.getValue())))
                .andExpect(jsonPath("$.[*].id").value(hasItem(JOB_ADVERTISEMENT_ID_02.getValue())));
    }

    @Test
    public void shouldFilterByCantons() throws Exception {
        // GIVEN
        Location location1 = new Location.Builder().setRemarks("remarks").setCity("city").setPostalCode("postalCode").setCommunalCode("communalCode")
                .setRegionCode("regionCode").setCountryIsoCode("ch")
                .setCantonCode("BE").build();
        Location location2 = new Location.Builder().setRemarks("remarks").setCity("city").setPostalCode("postalCode").setCommunalCode("communalCode")
                .setRegionCode("regionCode").setCountryIsoCode("ch")
                .setCantonCode("BE").build();
        Location location3 = new Location.Builder().setRemarks("remarks").setCity("city").setPostalCode("postalCode").setCommunalCode("communalCode")
                .setRegionCode("regionCode").setCountryIsoCode("ch")
                .setCantonCode("ZH").build();

        index(createJobWithLocation(JOB_ADVERTISEMENT_ID_01, location1));
        index(createJobWithLocation(JOB_ADVERTISEMENT_ID_02, location2));
        index(createJobWithLocation(JOB_ADVERTISEMENT_ID_03, location3));

        // WHEN
        JobAdvertisementSearchRequest searchRequest = new JobAdvertisementSearchRequest();
        searchRequest.setCantonCodes(new String[]{"BE"});

        ResultActions resultActions = mockMvc.perform(
                post(API_JOB_ADVERTISEMENTS + "/_search")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(searchRequest))
        );

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(header().string("X-Total-Count", "2"))
                .andExpect(jsonPath("$.[*].id").value(hasItem(JOB_ADVERTISEMENT_ID_01.getValue())))
                .andExpect(jsonPath("$.[*].id").value(hasItem(JOB_ADVERTISEMENT_ID_02.getValue())))
        ;
    }

    @Test
    public void shouldFilterByWorkingTimeMinMax() throws Exception {
        // GIVEN
        index(createJobWithWorkload(JOB_ADVERTISEMENT_ID_01, 1, 100));
        index(createJobWithWorkload(JOB_ADVERTISEMENT_ID_02, 80, 100));
        index(createJobWithWorkload(JOB_ADVERTISEMENT_ID_03, 50, 50));

        // WHEN
        JobAdvertisementSearchRequest searchRequest = new JobAdvertisementSearchRequest();
        searchRequest.setWorkloadPercentageMin(60);
        searchRequest.setWorkloadPercentageMax(80);

        ResultActions resultActions = mockMvc.perform(
                post(API_JOB_ADVERTISEMENTS + "/_search")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(searchRequest))
        );

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(header().string("X-Total-Count", "2"))
                .andExpect(jsonPath("$.[*].id").value(hasItem(JOB_ADVERTISEMENT_ID_01.getValue())))
                .andExpect(jsonPath("$.[*].id").value(hasItem(JOB_ADVERTISEMENT_ID_02.getValue())))
        ;
    }

    @Test
    public void shouldFilterByCompanyName() throws Exception {
        // GIVEN
        index(createJob(JOB_ADVERTISEMENT_ID_01));
        index(createJobWithCompanyName(JOB_ADVERTISEMENT_ID_02, "Gösser"));
        index(createJob(JOB_ADVERTISEMENT_ID_03));

        // WHEN
        JobAdvertisementSearchRequest searchRequest = new JobAdvertisementSearchRequest();
        searchRequest.setCompanyName("Goesser");

        ResultActions resultActions = mockMvc.perform(
                post(API_JOB_ADVERTISEMENTS + "/_search")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(searchRequest))
        );

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(header().string("X-Total-Count", "1"))
                .andExpect(jsonPath("$.*.id").value(JOB_ADVERTISEMENT_ID_02.getValue()))
        ;
    }

    @Test
    public void shouldFilterByPermanentContractType() throws Exception {
        // GIVEN
        index(createJob(JOB_ADVERTISEMENT_ID_01));
        index(createJobWithContractType(JOB_ADVERTISEMENT_ID_02, true));
        index(createJob(JOB_ADVERTISEMENT_ID_03));

        // WHEN
        JobAdvertisementSearchRequest searchRequest = new JobAdvertisementSearchRequest();
        searchRequest.setPermanent(true);

        ResultActions resultActions = mockMvc.perform(
                post(API_JOB_ADVERTISEMENTS + "/_search")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(searchRequest))
        );

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(header().string("X-Total-Count", "1"))
                .andExpect(jsonPath("$.*.id").value(JOB_ADVERTISEMENT_ID_02.getValue()))
        ;
    }

    @Test
    public void shouldFilterByDisplayRestricted() throws Exception {
        // GIVEN
        when(this.mockCurrentUserContext.hasRole(Role.JOBSEEKER_CLIENT)).thenReturn(true);
        index(createJob(JOB_ADVERTISEMENT_ID_01));
        index(createRestrictedJob(JOB_ADVERTISEMENT_ID_02));
        index(createJob(JOB_ADVERTISEMENT_ID_03));

        // WHEN
        JobAdvertisementSearchRequest searchRequest = new JobAdvertisementSearchRequest();
        searchRequest.setDisplayRestricted(true);

        ResultActions resultActions = mockMvc.perform(
                post(API_JOB_ADVERTISEMENTS + "/_search")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(searchRequest))
        );

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(header().string("X-Total-Count", "1"))
                .andExpect(jsonPath("$.*.id").value(JOB_ADVERTISEMENT_ID_02.getValue()))
        ;
    }

    @Test
    public void shouldFilterByPublicationForJobSeeker() throws Exception {
        // GIVEN
        when(this.mockCurrentUserContext.hasRole(Role.JOBSEEKER_CLIENT)).thenReturn(true);
        //-------------------------------------------------------------------------------publicDisplay  restrictedDisplay
        index(createJobWithoutPublicDisplayAndWithoutRestrictedDisplay(JOB_ADVERTISEMENT_ID_01));   //0 0
        index(createJobWithoutPublicDisplayAndWithRestrictedDisplay(JOB_ADVERTISEMENT_ID_02));      //0 1
        index(createJobWithPublicDisplayAndWithRestrictedDisplay(JOB_ADVERTISEMENT_ID_03));         //1 1
        index(createJobWithPublicDisplayAndWithoutRestrictedDisplay(JOB_ADVERTISEMENT_ID_04));      //1 0

        // WHEN
        JobAdvertisementSearchRequest searchRequest = new JobAdvertisementSearchRequest();

        ResultActions resultActions = mockMvc.perform(
                post(API_JOB_ADVERTISEMENTS + "/_search")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(searchRequest))
        );

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(header().string("X-Total-Count", "3"))
                .andExpect(jsonPath("$.[*].id").value(hasItem(JOB_ADVERTISEMENT_ID_02.getValue())))
                .andExpect(jsonPath("$.[*].id").value(hasItem(JOB_ADVERTISEMENT_ID_03.getValue())))
                .andExpect(jsonPath("$.[*].id").value(hasItem(JOB_ADVERTISEMENT_ID_04.getValue())))
        ;

    }

    @Test
    public void shouldFilterByPublicationForAnonymusUser() throws Exception {
        // GIVEN
        //-------------------------------------------------------------------------------publicDisplay  restrictedDisplay
        index(createJobWithoutPublicDisplayAndWithoutRestrictedDisplay(JOB_ADVERTISEMENT_ID_01));   //0 0
        index(createJobWithoutPublicDisplayAndWithRestrictedDisplay(JOB_ADVERTISEMENT_ID_02));      //0 1
        index(createJobWithPublicDisplayAndWithRestrictedDisplay(JOB_ADVERTISEMENT_ID_03));         //1 1
        index(createJobWithPublicDisplayAndWithoutRestrictedDisplay(JOB_ADVERTISEMENT_ID_04));      //1 0

        // WHEN
        JobAdvertisementSearchRequest searchRequest = new JobAdvertisementSearchRequest();

        ResultActions resultActions = mockMvc.perform(
                post(API_JOB_ADVERTISEMENTS + "/_search")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(searchRequest))
        );

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(header().string("X-Total-Count", "2"))
                .andExpect(jsonPath("$.[*].id").value(hasItem(JOB_ADVERTISEMENT_ID_03.getValue())))
                .andExpect(jsonPath("$.[*].id").value(hasItem(JOB_ADVERTISEMENT_ID_04.getValue())))
        ;

    }

    @Test
    public void shouldNotShowRestrictedJobsForAnonymusUsers() throws Exception {
        // GIVEN
        index(createRestrictedJob(JOB_ADVERTISEMENT_ID_01));
        index(createJob(JOB_ADVERTISEMENT_ID_02));
        index(createRestrictedJob(JOB_ADVERTISEMENT_ID_03));

        // WHEN
        JobAdvertisementSearchRequest searchRequest = new JobAdvertisementSearchRequest();

        ResultActions resultActions = mockMvc.perform(
                post(API_JOB_ADVERTISEMENTS + "/_search")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(searchRequest))
        );

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(header().string("X-Total-Count", "1"))
                .andExpect(jsonPath("$.*.id").value(JOB_ADVERTISEMENT_ID_02.getValue()));
    }

    @Test
    public void shouldShowRestrictedJobsForJobSeekers() throws Exception {
        // GIVEN
        when(this.mockCurrentUserContext.hasRole(Role.JOBSEEKER_CLIENT)).thenReturn(true);
        index(createRestrictedJob(JOB_ADVERTISEMENT_ID_01));
        index(createJob(JOB_ADVERTISEMENT_ID_02));
        index(createRestrictedJob(JOB_ADVERTISEMENT_ID_03));

        // WHEN
        JobAdvertisementSearchRequest searchRequest = new JobAdvertisementSearchRequest();

        ResultActions resultActions = mockMvc.perform(
                post(API_JOB_ADVERTISEMENTS + "/_search")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(searchRequest))
        );

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(header().string("X-Total-Count", "3"))
                .andExpect(jsonPath("$.[*].id").value(hasItem(JOB_ADVERTISEMENT_ID_01.getValue())))
                .andExpect(jsonPath("$.[*].id").value(hasItem(JOB_ADVERTISEMENT_ID_02.getValue())))
                .andExpect(jsonPath("$.[*].id").value(hasItem(JOB_ADVERTISEMENT_ID_03.getValue())))
        ;
    }

    @Test
    public void countJobs() throws Exception {
        // GIVEN
        index(createJob(JOB_ADVERTISEMENT_ID_01));
        index(createJob(JOB_ADVERTISEMENT_ID_02));
        index(createJobWithContractType(JOB_ADVERTISEMENT_ID_03, true));
        index(createJobWithWorkload(JOB_ADVERTISEMENT_ID_04, 20, 50));
        index(createJobWithDescription(JOB_ADVERTISEMENT_ID_05, "xxx", "yyyy"));
        index(createJob(JOB_ADVERTISEMENT_ID_06));
        index(createJob(JOB_ADVERTISEMENT_ID_07));
        index(createJob(JOB_ADVERTISEMENT_ID_08));
        index(createJob(JOB_ADVERTISEMENT_ID_09));
        index(createJob(JOB_ADVERTISEMENT_ID_10));

        // WHEN
        JobAdvertisementSearchRequest searchRequest = new JobAdvertisementSearchRequest();
        searchRequest.setPermanent(false);
        searchRequest.setWorkloadPercentageMin(70);
        searchRequest.setCantonCodes(new String[]{"BE"});
        searchRequest.setProfessionCodes(new ProfessionCode[]{new ProfessionCode(ProfessionCodeType.AVAM, "avamOccupationCode")});
        searchRequest.setKeywords(new String[]{"title"});

        ResultActions resultActions = mockMvc.perform(
                post(API_JOB_ADVERTISEMENTS + "/_count")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(searchRequest))
        );

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.totalCount").value(7))
        ;
    }

    private void index(JobAdvertisement jobAdvertisement) {
        this.jobAdvertisementElasticsearchRepository.save(new JobAdvertisementDocument(jobAdvertisement));

    }

    private JobAdvertisement createJob(JobAdvertisementId jobAdvertisementId) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setOwner(createOwner(jobAdvertisementId))
                .setPublication(createPublication())
                .setJobContent(createJobContent(jobAdvertisementId))
                .build();

    }

    private JobAdvertisement createJobWithOwnerAndPublicationStartDate(JobAdvertisementId jobAdvertisementId, String companyId, LocalDate startDate) {
        Publication publication = new Publication.Builder()
                .setStartDate(startDate)
                .setEndDate(startDate.plusDays(5))
                .build();

        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setOwner(createOwnerWithCompanyId(jobAdvertisementId, companyId))
                .setPublication(publication)
                .setJobContent(new JobContent.Builder()
                        .setTitle(String.format("title-%s", jobAdvertisementId.getValue()))
                        .setDescription(String.format("description-%s", jobAdvertisementId.getValue()))
                        .setDisplayCompany(createCompany(jobAdvertisementId))
                        .setCompany(createCompany(jobAdvertisementId))
                        .setLanguageSkills(Collections.singletonList(createLanguageSkill()))
                        .setEmployment(createEmployment())
                        .setPublicContact(createPublicContact(jobAdvertisementId))
                        .setApplyChannel(createApplyChannel())
                        .setLocation(createLocation())
                        .setOccupations(Collections.singletonList(createOccupation()))
                        .build())
                .build();

    }


    private JobAdvertisement createJobWithoutPublicDisplayAndWithoutRestrictedDisplay(JobAdvertisementId jobAdvertisementId) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setOwner(createOwner(jobAdvertisementId))
                .setPublication(createPublication(false, false))
                .setJobContent(createJobContent(jobAdvertisementId))
                .setReportingObligation(true)
                .build();

    }

    private JobAdvertisement createJobWithPublicDisplayAndWithRestrictedDisplay(JobAdvertisementId jobAdvertisementId) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setOwner(createOwner(jobAdvertisementId))
                .setPublication(createPublication(true, true))
                .setJobContent(createJobContent(jobAdvertisementId))
                .setReportingObligation(true)
                .build();

    }

    private JobAdvertisement createJobWithoutPublicDisplayAndWithRestrictedDisplay(JobAdvertisementId jobAdvertisementId) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setOwner(createOwner(jobAdvertisementId))
                .setPublication(createPublication(false, true))
                .setJobContent(createJobContent(jobAdvertisementId))
                .setReportingObligation(true)
                .build();

    }

    private JobAdvertisement createJobWithPublicDisplayAndWithoutRestrictedDisplay(JobAdvertisementId jobAdvertisementId) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setOwner(createOwner(jobAdvertisementId))
                .setPublication(createPublication(true, false))
                .setJobContent(createJobContent(jobAdvertisementId))
                .setReportingObligation(true)
                .build();

    }

    private JobAdvertisement createJobWithPublicationWithoutPublicDisplay(JobAdvertisementId jobAdvertisementId) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setOwner(createOwner(jobAdvertisementId))
                .setPublication(createPublication(false, true))
                .setJobContent(createJobContent(jobAdvertisementId))
                .setReportingObligation(true)
                .build();

    }

    private JobAdvertisement createRestrictedJob(JobAdvertisementId jobAdvertisementId) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStatus(JobAdvertisementStatus.PUBLISHED_RESTRICTED)
                .setOwner(createOwner(jobAdvertisementId))
                .setPublication(createPublication())
                .setJobContent(createJobContent(jobAdvertisementId))
                .setReportingObligation(true)
                .build();

    }

    private JobAdvertisement createJobWithContractType(JobAdvertisementId jobAdvertisementId, boolean isPermanent) {

        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setOwner(createOwner(jobAdvertisementId))
                .setPublication(createPublication())
                .setJobContent(new JobContent.Builder()
                        .setTitle(String.format("title-%s", jobAdvertisementId.getValue()))
                        .setDescription(String.format("description-%s", jobAdvertisementId.getValue()))
                        .setDisplayCompany(createCompany(jobAdvertisementId))
                        .setCompany(createCompany(jobAdvertisementId))
                        .setLanguageSkills(Collections.singletonList(createLanguageSkill()))
                        .setEmployment(new Employment.Builder()
                                .setStartDate(TimeMachine.now().toLocalDate())
                                .setShortEmployment(false)
                                .setImmediately(false)
                                .setPermanent(isPermanent)
                                .setWorkloadPercentageMin(80)
                                .setWorkloadPercentageMax(100)
                                .build())
                        .setPublicContact(createPublicContact(jobAdvertisementId))
                        .setApplyChannel(createApplyChannel())
                        .setLocation(createLocation())
                        .setOccupations(Collections.singletonList(createOccupation()))
                        .build())
                .build();

    }

    private JobAdvertisement createJobWithCompanyName(JobAdvertisementId jobAdvertisementId, String companyName) {

        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setOwner(createOwner(jobAdvertisementId))
                .setPublication(createPublication())
                .setJobContent(new JobContent.Builder()
                        .setTitle(String.format("title-%s", jobAdvertisementId.getValue()))
                        .setDescription(String.format("description-%s", jobAdvertisementId.getValue()))
                        .setDisplayCompany(createCompany(companyName))
                        .setCompany(createCompany(companyName))
                        .setLanguageSkills(Collections.singletonList(createLanguageSkill()))
                        .setEmployment(createEmployment())
                        .setPublicContact(createPublicContact(jobAdvertisementId))
                        .setApplyChannel(createApplyChannel())
                        .setLocation(createLocation())
                        .setOccupations(Collections.singletonList(createOccupation()))
                        .build())
                .build();

    }

    private JobAdvertisement createJobWithDescription(JobAdvertisementId jobAdvertisementId, String title, String description) {
        return createJobWithDescription(jobAdvertisementId, title, description, SourceSystem.JOBROOM);
    }

    private JobAdvertisement createJobWithDescriptionAndOwnerCompanyId(JobAdvertisementId jobAdvertisementId, String title, String description, String companyId) {
        Owner owner = createOwnerWithCompanyId(jobAdvertisementId, companyId);
        return createJobWithDescription(jobAdvertisementId, title, description, SourceSystem.JOBROOM, owner);
    }

    private Owner createOwnerWithCompanyId(JobAdvertisementId jobAdvertisementId, String companyId) {
        return new Owner.Builder()
                .setAccessToken(String.format("access-token-%s", jobAdvertisementId.getValue()))
                .setCompanyId(companyId)
                .setUserId(String.format("user-id-%s", jobAdvertisementId.getValue()))
                .build();
    }

    private JobAdvertisement createJobWithDescription(JobAdvertisementId jobAdvertisementId, String title, String description, SourceSystem sourceSystem) {
        return createJobWithDescription(jobAdvertisementId, title, description, sourceSystem, createOwner(jobAdvertisementId));
    }

    private JobAdvertisement createJobWithLanguageSkills(JobAdvertisementId jobAdvertisementId, String title, String description, SourceSystem sourceSystem, LanguageSkill... languageSkills) {
        return createJobWithDescription(jobAdvertisementId, title, description, sourceSystem, createOwner(jobAdvertisementId), languageSkills);
    }

    private JobAdvertisement createJobWithDescription(JobAdvertisementId jobAdvertisementId, String title, String description, SourceSystem sourceSystem, Owner owner, LanguageSkill... languageSkills) {
        List<LanguageSkill> skills = languageSkills.length == 0 ? Collections.singletonList(createLanguageSkill()) : Arrays.asList(languageSkills);
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(sourceSystem)
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setOwner(owner)
                .setPublication(createPublication())
                .setJobContent(new JobContent.Builder()
                        .setTitle(String.format("title-%s", jobAdvertisementId.getValue()))
                        .setDescription(String.format("description-%s", jobAdvertisementId.getValue()))
                        .setDisplayCompany(createCompany(jobAdvertisementId))
                        .setCompany(createCompany(jobAdvertisementId))
                        .setLanguageSkills(skills)
                        .setEmployment(createEmployment())
                        .setPublicContact(createPublicContact(jobAdvertisementId))
                        .setApplyChannel(createApplyChannel())
                        .setLocation(createLocation())
                        .setOccupations(Collections.singletonList(createOccupation()))
                        .build())
                .build();
    }

    private JobAdvertisement createJobWithWorkload(JobAdvertisementId jobAdvertisementId, int workloadPercentageMin, int workloadPercentageMax) {

        Employment employment = new Employment.Builder()
                .setStartDate(TimeMachine.now().toLocalDate())
                .setEndDate(TimeMachine.now().plusDays(31).toLocalDate())
                .setShortEmployment(false)
                .setImmediately(false)
                .setPermanent(false)
                .setWorkloadPercentageMin(workloadPercentageMin)
                .setWorkloadPercentageMax(workloadPercentageMax)
                .build();

        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setOwner(createOwner(jobAdvertisementId))
                .setPublication(createPublication())
                .setJobContent(new JobContent.Builder()
                        .setTitle(String.format("title-%s", jobAdvertisementId.getValue()))
                        .setDescription(String.format("description-%s", jobAdvertisementId.getValue()))
                        .setDisplayCompany(createCompany(jobAdvertisementId))
                        .setCompany(createCompany(jobAdvertisementId))
                        .setLanguageSkills(Collections.singletonList(createLanguageSkill()))
                        .setEmployment(employment)
                        .setPublicContact(createPublicContact(jobAdvertisementId))
                        .setApplyChannel(createApplyChannel())
                        .setLocation(createLocation())
                        .setOccupations(Collections.singletonList(createOccupation()))
                        .build())
                .build();
    }

    private JobAdvertisement createJobWithX28Code(JobAdvertisementId jobAdvertisementId, String x28Codes) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setOwner(createOwner(jobAdvertisementId))
                .setPublication(createPublication())
                .setJobContent(new JobContent.Builder()
                        .setTitle(String.format("title-%s", jobAdvertisementId.getValue()))
                        .setDescription(String.format("description-%s", jobAdvertisementId.getValue()))
                        .setX28OccupationCodes(x28Codes)
                        .setDisplayCompany(createCompany(jobAdvertisementId))
                        .setCompany(createCompany(jobAdvertisementId))
                        .setLanguageSkills(Collections.singletonList(createLanguageSkill()))
                        .setEmployment(createEmployment())
                        .setPublicContact(createPublicContact(jobAdvertisementId))
                        .setApplyChannel(createApplyChannel())
                        .setLocation(createLocation())
                        .setOccupations(Collections.singletonList(createOccupation()))
                        .build())
                .build();
    }

    private JobAdvertisement createJobWithOccupation(JobAdvertisementId jobAdvertisementId, Occupation occupation) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setOwner(createOwner(jobAdvertisementId))
                .setPublication(createPublication())
                .setJobContent(new JobContent.Builder()
                        .setTitle(String.format("title-%s", jobAdvertisementId.getValue()))
                        .setDescription(String.format("description-%s", jobAdvertisementId.getValue()))
                        .setDisplayCompany(createCompany(jobAdvertisementId))
                        .setCompany(createCompany(jobAdvertisementId))
                        .setLanguageSkills(Collections.singletonList(createLanguageSkill()))
                        .setEmployment(createEmployment())
                        .setPublicContact(createPublicContact(jobAdvertisementId))
                        .setApplyChannel(createApplyChannel())
                        .setLocation(createLocation())
                        .setOccupations(Collections.singletonList(occupation))
                        .build())
                .build();
    }

    private JobAdvertisement createJobWithLocation(JobAdvertisementId jobAdvertisementId, Location location) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setOwner(createOwner(jobAdvertisementId))
                .setPublication(createPublication())
                .setJobContent(new JobContent.Builder()
                        .setTitle(String.format("title-%s", jobAdvertisementId.getValue()))
                        .setDescription(String.format("description-%s", jobAdvertisementId.getValue()))
                        .setDisplayCompany(createCompany(jobAdvertisementId))
                        .setCompany(createCompany(jobAdvertisementId))
                        .setLanguageSkills(Collections.singletonList(createLanguageSkill()))
                        .setEmployment(createEmployment())
                        .setPublicContact(createPublicContact(jobAdvertisementId))
                        .setApplyChannel(createApplyChannel())
                        .setLocation(location)
                        .setOccupations(Collections.singletonList(createOccupation()))
                        .build())
                .build();
    }

}
