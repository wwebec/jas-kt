package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem.API;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem.EXTERN;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementIdFixture.job01;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementIdFixture.job02;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementIdFixture.job03;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementIdFixture.job04;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementIdFixture.job05;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementIdFixture.job06;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementIdFixture.job07;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementIdFixture.job08;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementIdFixture.job09;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementIdFixture.job10;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementTestFixture.createJob;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementTestFixture.createJobWithCompanyName;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementTestFixture.createJobWithContractType;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementTestFixture.createJobWithDescription;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementTestFixture.createJobWithDescriptionAndOwnerCompanyId;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementTestFixture.createJobWithLanguageSkills;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementTestFixture.createJobWithLocation;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementTestFixture.createJobWithOccupation;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementTestFixture.createJobWithOwnerAndPublicationStartDate;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementTestFixture.createJobWithPublicDisplayAndWithRestrictedDisplay;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementTestFixture.createJobWithPublicDisplayAndWithoutRestrictedDisplay;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementTestFixture.createJobWithWorkload;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementTestFixture.createJobWithX28Code;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementTestFixture.createJobWithoutPublicDisplayAndWithRestrictedDisplay;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementTestFixture.createJobWithoutPublicDisplayAndWithoutRestrictedDisplay;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementTestFixture.createRestrictedJob;
import static java.time.LocalDate.now;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import ch.admin.seco.jobs.services.jobadservice.Application;
import ch.admin.seco.jobs.services.jobadservice.application.security.CurrentUserContext;
import ch.admin.seco.jobs.services.jobadservice.application.security.Role;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementRepository;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageLevel;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageSkill;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Location;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Occupation;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.OwnerFixture;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.ElasticsearchConfiguration;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.read.jobadvertisement.JobAdvertisementSearchRequest;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.read.jobadvertisement.JobAdvertisementSearchService;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.read.jobadvertisement.PeaJobAdvertisementSearchRequest;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.read.jobadvertisement.ProfessionCode;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.read.jobadvertisement.ProfessionCodeType;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.write.jobadvertisement.JobAdvertisementDocument;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.write.jobadvertisement.JobAdvertisementElasticsearchRepository;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.TestUtil;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller.errors.ExceptionTranslator;

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

    @Test
    public void shouldSearchWithoutQuery() throws Exception {
        // GIVEN
        index(createJob(job01.id()));
        index(createJob(job02.id()));
        index(createJob(job03.id()));

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
        index(createJob(job01.id()));
        index(createJob(job02.id()));
        index(createJob(job03.id()));

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
        index(createJobWithDescriptionAndOwnerCompanyId(job01.id(), "c++ developer", "c++ & java entwickler", "company-1"));
        index(createJobWithDescriptionAndOwnerCompanyId(job02.id(), "java & javascript developer", "jee entwickler", "company-1"));
        index(createJobWithDescriptionAndOwnerCompanyId(job03.id(), "php programmierer", "php programierer", "company-2"));
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
        index(createJobWithOwnerAndPublicationStartDate(job01.id(), "company-1", now().minusDays(30)));
        index(createJobWithOwnerAndPublicationStartDate(job02.id(), "company-1", now().minusDays(7)));
        index(createJobWithOwnerAndPublicationStartDate(job03.id(), "company-1", now()));
        index(createJob(job04.id()));
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
        index(createJobWithDescriptionAndOwnerCompanyId(job01.id(), "c++ developer", "c++ & java entwickler", "company-1"));
        index(createJobWithDescriptionAndOwnerCompanyId(job02.id(), "java & javascript developer", "jee entwickler", "company-1"));
        index(createJobWithDescriptionAndOwnerCompanyId(job03.id(), "php programmierer", "php programierer", "company-2"));
        index(createJobWithDescriptionAndOwnerCompanyId(job04.id(), "javascript developer", "javascript developer", "company-3"));
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
        index(createJobWithDescription(job01.id(), "c++ developer", "c++ & java entwickler"));
        index(createJobWithDescription(job02.id(), "java & javascript developer", "jee entwickler"));
        index(createJobWithDescription(job03.id(), "php programmierer", "php programierer"));

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

                .andExpect(jsonPath("$.[0].id").value(equalTo(job01.name())))
                .andExpect(jsonPath("$.[0].jobContent.jobDescriptions[0].title").value(equalTo("c++ developer")))
                .andExpect(jsonPath("$.[0].jobContent.jobDescriptions[0].description").value(equalTo("c++ &amp; <em>java</em> <em>entwickler</em>")))

                .andExpect(jsonPath("$.[1].id").value(equalTo(job02.name())))
                .andExpect(jsonPath("$.[1].jobContent.jobDescriptions[0].title").value(equalTo("<em>java</em> & <em>javascript</em> developer")))
                .andExpect(jsonPath("$.[1].jobContent.jobDescriptions[0].description").value(equalTo("jee <em>entwickler</em>")));
    }

    @Test
    public void shouldSearchBySourceSystemKeyword() throws Exception {
        // GIVEN
        index(createJobWithDescription(job01.id(), "c++ developer", "c++ & java entwickler", EXTERN, OwnerFixture.of(job01.id()).build()));
        index(createJobWithDescription(job02.id(), "java & javascript developer", "jee entwickler", API, OwnerFixture.of(job02.id()).build()));

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

                .andExpect(jsonPath("$.[0].id").value(equalTo(job01.name())))
                .andExpect(jsonPath("$.[0].jobContent.jobDescriptions[0].title").value(equalTo("c++ developer")))
                .andExpect(jsonPath("$.[0].jobContent.jobDescriptions[0].description").value(equalTo("c++ &amp; java entwickler")));
    }

    @Test
    public void shouldSearchByLanguageSkillKeyword() throws Exception {
        // GIVEN
        final LanguageSkill da = new LanguageSkill.Builder().setLanguageIsoCode("da").setSpokenLevel(LanguageLevel.PROFICIENT).setWrittenLevel(LanguageLevel.INTERMEDIATE).build();
        final LanguageSkill en = new LanguageSkill.Builder().setLanguageIsoCode("en").setSpokenLevel(LanguageLevel.PROFICIENT).setWrittenLevel(LanguageLevel.INTERMEDIATE).build();

        index(createJobWithLanguageSkills(job01.id(), "c++ developer", "c++ & java entwickler", EXTERN, da));
        index(createJobWithLanguageSkills(job02.id(), "java & javascript developer", "jee entwickler", API, en));

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

        index(createJobWithOccupation(job01.id(), occupation1));
        index(createJobWithOccupation(job02.id(), occupation2));
        index(createJobWithOccupation(job03.id(), occupation3));
        index(createJobWithOccupation(job04.id(), occupation4));

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
                .andExpect(jsonPath("$.[*].id").value(hasItem(job01.name())))
                .andExpect(jsonPath("$.[*].id").value(hasItem(job04.name())))
        ;
    }

    @Test
    public void shouldSearchByOccupation_X28() throws Exception {

        // GIVEN
        index(createJobWithX28Code(job01.id(), "1111,2222"));
        index(createJobWithX28Code(job02.id(), "1111"));
        index(createJobWithX28Code(job03.id(), "3333"));
        index(createJobWithX28Code(job04.id(), "4444"));

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
                .andExpect(jsonPath("$.[*].id").value(hasItem(job01.name())))
                .andExpect(jsonPath("$.[*].id").value(hasItem(job02.name())));
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

        index(createJobWithLocation(job01.id(), location1));
        index(createJobWithLocation(job02.id(), location2));
        index(createJobWithLocation(job03.id(), location3));

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
                .andExpect(jsonPath("$.[*].id").value(hasItem(job01.name())))
                .andExpect(jsonPath("$.[*].id").value(hasItem(job02.name())))
        ;
    }

    @Test
    public void shouldFilterByWorkingTimeMinMax() throws Exception {
        // GIVEN
        index(createJobWithWorkload(job01.id(), 1, 100));
        index(createJobWithWorkload(job02.id(), 80, 100));
        index(createJobWithWorkload(job03.id(), 50, 50));

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
                .andExpect(jsonPath("$.[*].id").value(hasItem(job01.name())))
                .andExpect(jsonPath("$.[*].id").value(hasItem(job02.name())))
        ;
    }

    @Test
    public void shouldFilterByCompanyName() throws Exception {
        // GIVEN
        index(createJob(job01.id()));
        index(createJobWithCompanyName(job02.id(), "Gösser"));
        index(createJob(job03.id()));

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
                .andExpect(jsonPath("$.*.id").value(job02.name()))
        ;
    }

    @Test
    public void shouldFilterByPermanentContractType() throws Exception {
        // GIVEN
        index(createJob(job01.id()));
        index(createJobWithContractType(job02.id(), true));
        index(createJob(job03.id()));

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
                .andExpect(jsonPath("$.*.id").value(job02.name()))
        ;
    }

    @Test
    public void shouldFilterByDisplayRestricted() throws Exception {
        // GIVEN
        when(this.mockCurrentUserContext.hasRole(Role.JOBSEEKER_CLIENT)).thenReturn(true);
        index(createJob(job01.id()));
        index(createRestrictedJob(job02.id()));
        index(createJob(job03.id()));

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
                .andExpect(jsonPath("$.*.id").value(job02.name()))
        ;
    }

    @Test
    public void shouldFilterByPublicationForJobSeeker() throws Exception {
        // GIVEN
        when(this.mockCurrentUserContext.hasRole(Role.JOBSEEKER_CLIENT)).thenReturn(true);
        //-------------------------------------------------------------------------------publicDisplay  restrictedDisplay
        index(createJobWithoutPublicDisplayAndWithoutRestrictedDisplay(job01.id()));   //0 0
        index(createJobWithoutPublicDisplayAndWithRestrictedDisplay(job02.id()));      //0 1
        index(createJobWithPublicDisplayAndWithRestrictedDisplay(job03.id()));         //1 1
        index(createJobWithPublicDisplayAndWithoutRestrictedDisplay(job04.id()));      //1 0

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
                .andExpect(jsonPath("$.[*].id").value(hasItem(job02.name())))
                .andExpect(jsonPath("$.[*].id").value(hasItem(job03.name())))
                .andExpect(jsonPath("$.[*].id").value(hasItem(job04.name())))
        ;

    }

    @Test
    public void shouldFilterByPublicationForAnonymusUser() throws Exception {
        // GIVEN
        //-------------------------------------------------------------------------------publicDisplay  restrictedDisplay
        index(createJobWithoutPublicDisplayAndWithoutRestrictedDisplay(job01.id()));   //0 0
        index(createJobWithoutPublicDisplayAndWithRestrictedDisplay(job02.id()));      //0 1
        index(createJobWithPublicDisplayAndWithRestrictedDisplay(job03.id()));         //1 1
        index(createJobWithPublicDisplayAndWithoutRestrictedDisplay(job04.id()));      //1 0

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
                .andExpect(jsonPath("$.[*].id").value(hasItem(job03.name())))
                .andExpect(jsonPath("$.[*].id").value(hasItem(job04.name())))
        ;

    }

    @Test
    public void shouldNotShowRestrictedJobsForAnonymusUsers() throws Exception {
        // GIVEN
        index(createRestrictedJob(job01.id()));
        index(createJob(job02.id()));
        index(createRestrictedJob(job03.id()));

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
                .andExpect(jsonPath("$.*.id").value(job02.name()));
    }

    @Test
    public void shouldShowRestrictedJobsForJobSeekers() throws Exception {
        // GIVEN
        when(this.mockCurrentUserContext.hasRole(Role.JOBSEEKER_CLIENT)).thenReturn(true);
        index(createRestrictedJob(job01.id()));
        index(createJob(job02.id()));
        index(createRestrictedJob(job03.id()));

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
                .andExpect(jsonPath("$.[*].id").value(hasItem(job01.name())))
                .andExpect(jsonPath("$.[*].id").value(hasItem(job02.name())))
                .andExpect(jsonPath("$.[*].id").value(hasItem(job03.name())))
        ;
    }

    @Test
    public void countJobs() throws Exception {
        // GIVEN
        index(createJob(job01.id()));
        index(createJob(job02.id()));
        index(createJobWithContractType(job03.id(), true));
        index(createJobWithWorkload(job04.id(), 20, 50));
        index(createJobWithDescription(job05.id(), "xxx", "yyyy"));
        index(createJob(job06.id()));
        index(createJob(job07.id()));
        index(createJob(job08.id()));
        index(createJob(job09.id()));
        index(createJob(job10.id()));

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
}
