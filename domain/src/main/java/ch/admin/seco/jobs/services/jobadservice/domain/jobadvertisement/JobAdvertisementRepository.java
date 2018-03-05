package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.stream.Stream;


@Transactional(propagation = Propagation.MANDATORY)
public interface JobAdvertisementRepository extends JpaRepository<JobAdvertisement, JobAdvertisementId> {
    @Query("select j from JobAdvertisement j where j.status = ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.PUBLISHED_RESTRICTED and j.reportingObligationEndDate < :currentDate")
    Stream<JobAdvertisement> findAllWhereBlackoutNeedToExpire(@Param("currentDate") LocalDate currentDate);
}
