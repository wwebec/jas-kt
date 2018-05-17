package ch.admin.seco.jobs.services.jobadservice.domain.apiuser;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ApiUserRepository extends JpaRepository<ApiUser, ApiUserId> {
	@Query("select au from ApiUser au")
	Stream<ApiUser> streamAll();
}
