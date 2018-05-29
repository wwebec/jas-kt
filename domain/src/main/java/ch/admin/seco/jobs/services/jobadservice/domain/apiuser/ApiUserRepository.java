package ch.admin.seco.jobs.services.jobadservice.domain.apiuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.stream.Stream;

public interface ApiUserRepository extends JpaRepository<ApiUser, ApiUserId> {
    @Query("select au from ApiUser au")
    Stream<ApiUser> streamAll();

    ApiUser findByUsername(String username);
}
