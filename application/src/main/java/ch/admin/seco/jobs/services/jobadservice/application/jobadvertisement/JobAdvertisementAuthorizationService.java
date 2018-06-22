package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.application.security.CurrentUser;
import ch.admin.seco.jobs.services.jobadservice.application.security.CurrentUserContext;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Optional;

@Component
@Transactional
public class JobAdvertisementAuthorizationService {

    private final JobAdvertisementRepository jobAdvertisementRepository;

    private final CurrentUserContext currentUserContext;

    public JobAdvertisementAuthorizationService(JobAdvertisementRepository jobAdvertisementRepository, CurrentUserContext currentUserContext) {
        this.jobAdvertisementRepository = jobAdvertisementRepository;
        this.currentUserContext = currentUserContext;
    }

    public boolean canCancel(String jobAdvertisementId, String token) {
        Optional<JobAdvertisement> jobAdvertisement = this.jobAdvertisementRepository.findById(new JobAdvertisementId(jobAdvertisementId));
        if (!jobAdvertisement.isPresent()) {
            return true;
        }

        CurrentUser currentUser = this.currentUserContext.getCurrentUser();
        if (currentUser == null) {
            return hasToken(jobAdvertisement.get(), token);
        }

        return isOwner(jobAdvertisement.get(), currentUser);

    }

    private boolean hasToken(JobAdvertisement jobAdvertisement, String token) {
        return jobAdvertisement.getOwner().getAccessToken().equals(token);
    }

    public boolean isOwner(JobAdvertisement jobAdvertisement, CurrentUser currentUser) {

        String userId = currentUser.getUserId();
        if ((userId != null) && userId.equals(jobAdvertisement.getOwner().getUserId())) {
            return true;
        }

        String companyId = currentUser.getCompanyId();
        if ((companyId != null) && companyId.equals(jobAdvertisement.getOwner().getCompanyId())) {
            return true;
        }

        return false;
    }
}
