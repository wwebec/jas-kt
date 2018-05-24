package ch.admin.seco.jobs.services.jobadservice.domain.apiuser;

import ch.admin.seco.jobs.services.jobadservice.core.domain.TestDataProvider;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ApiUserTestDataProvider implements TestDataProvider<ApiUser> {

    public static final ApiUserId API_USER_ID_01 = new ApiUserId("apiUser01");
    public static final ApiUserId API_USER_ID_02 = new ApiUserId("apiUser02");

    private List<ApiUser> apiUsers;

    @Override
    public List<ApiUser> getTestData() {
        if (apiUsers == null) {
            apiUsers = new ArrayList<>();
            apiUsers.add(createApiUser01());
            apiUsers.add(createApiUser02());
        }
        return apiUsers;
    }

    public static ApiUser createApiUser01() {
        return new ApiUser.Builder()
                .setId(API_USER_ID_01)
                .setUsername("username-01")
                .setPassword("password-01")
                .setCompanyName("companyName-01")
                .setCompanyEmail("companyEmail-01@example.com")
                .setTechnicalContactName("technicalContactName-01")
                .setTechnicalContactEmail("technicalContactEmail-01@example.com")
                .setActive(true)
                .setCreateDate(LocalDate.of(2018, 1, 1))
                .build();
    }

    public static ApiUser createApiUser02() {
        return new ApiUser.Builder()
                .setId(API_USER_ID_02)
                .setUsername("username-02")
                .setPassword("password-02")
                .setCompanyName("companyName-02")
                .setCompanyEmail("companyEmail-02@example.com")
                .setTechnicalContactName("technicalContactName-02")
                .setTechnicalContactEmail("technicalContactEmail-02@example.com")
                .setActive(true)
                .setCreateDate(LocalDate.of(2018, 2, 2))
                .build();
    }

}
