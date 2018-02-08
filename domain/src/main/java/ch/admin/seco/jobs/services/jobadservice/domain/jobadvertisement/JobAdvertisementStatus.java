package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum JobAdvertisementStatus {

    CREATED,
    AVAM,
    APPROVED,
    REFINED,
    REJECTED,
    PUBLISHED,
    CANCELLED;

    static {
        CREATED.allowedDestinationStates = new JobAdvertisementStatus[]{AVAM, APPROVED};
        AVAM.allowedDestinationStates = new JobAdvertisementStatus[]{APPROVED,REJECTED};
        // TODO And so on ... tbd
        PUBLISHED.allowedDestinationStates = new JobAdvertisementStatus[]{CANCELLED};
    }

    private JobAdvertisementStatus[] allowedDestinationStates;

    public JobAdvertisementStatus validateTransitionTo(JobAdvertisementStatus destination){
        if (!Arrays.asList(allowedDestinationStates).contains(destination)) {
            throw new IllegalJobAdvertisementStatusTransitionException(this, destination);
        }
        return destination;
    }

    public static boolean isEqualOrForwardDestination(JobAdvertisementStatus source, JobAdvertisementStatus destination) {
        if (isEqualOrAllowedDestination(source, destination)) {
            return true;
        } else {
            HashSet<JobAdvertisementStatus> allowedDestinationStates = new HashSet<>();
            fillAllowedDestinations(source, allowedDestinationStates);
            return allowedDestinationStates.contains(destination);
        }
    }

    private static void fillAllowedDestinations(JobAdvertisementStatus jobAdvertisementStatus, Set<JobAdvertisementStatus> allowedDestinations) {
        if (Collections.addAll(allowedDestinations, jobAdvertisementStatus.allowedDestinationStates)) {
            for (JobAdvertisementStatus status : jobAdvertisementStatus.allowedDestinationStates) {
                fillAllowedDestinations(status, allowedDestinations);
            }
        }
    }

    private static boolean isEqualOrAllowedDestination(JobAdvertisementStatus source, JobAdvertisementStatus destination) {
        return source.equals(destination) || Arrays.asList(source.allowedDestinationStates).contains(destination);
    }

}
