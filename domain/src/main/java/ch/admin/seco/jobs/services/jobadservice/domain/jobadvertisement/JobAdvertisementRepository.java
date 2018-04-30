package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.QueryHint;

import static org.hibernate.jpa.QueryHints.HINT_CACHE_MODE;
import static org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE;

@Transactional(propagation = Propagation.MANDATORY)
public interface JobAdvertisementRepository extends JpaRepository<JobAdvertisement, JobAdvertisementId> {
    @Query("select j from JobAdvertisement j " +
            "where j.status = ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.PUBLISHED_RESTRICTED and j.reportingObligationEndDate < :currentDate")
    Stream<JobAdvertisement> findAllWhereBlackoutNeedToExpire(@Param("currentDate") LocalDate currentDate);

    @Query("select j from JobAdvertisement j " +
            "where j.status = ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.PUBLISHED_PUBLIC and j.publication.endDate < :currentDate")
    Stream<JobAdvertisement> findAllWherePublicationNeedToExpire(@Param("currentDate") LocalDate currentDate);

    Optional<JobAdvertisement> findByStellennummerAvam(String stellennummerAvam);

    Optional<JobAdvertisement> findByStellennummerEgov(String stellennummerEgov);

    Optional<JobAdvertisement> findByFingerprint(String fingerprint);

    @Query("select j from JobAdvertisement j where j.owner.accessToken = :accessToken")
    Optional<JobAdvertisement> findOneByAccessToken(@Param("accessToken") String accessToken);

    @QueryHints({
            @QueryHint(name = HINT_FETCH_SIZE, value = "1000"),
            @QueryHint(name = HINT_CACHE_MODE, value = "IGNORE")})
    @Query("select j from JobAdvertisement j " +
            "where j.status = ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.PUBLISHED_RESTRICTED " +
            "or j.status = ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.PUBLISHED_PUBLIC ")
    Stream<JobAdvertisement> streamAllPublished();
}
