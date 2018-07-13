package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum JobAdvertisementStatus {

    CREATED,
    INSPECTING,
    APPROVED,
    REJECTED,
    REFINING,
    PUBLISHED_RESTRICTED,
    PUBLISHED_PUBLIC,
    CANCELLED,
    ARCHIVED;

    static {
        CREATED.allowedDestinationStates = new JobAdvertisementStatus[]{CANCELLED, INSPECTING, REFINING};
        INSPECTING.allowedDestinationStates = new JobAdvertisementStatus[]{REFINING, APPROVED, REJECTED};
        APPROVED.allowedDestinationStates = new JobAdvertisementStatus[]{CANCELLED, REFINING};
        REJECTED.allowedDestinationStates = new JobAdvertisementStatus[]{};
        REFINING.allowedDestinationStates = new JobAdvertisementStatus[]{CANCELLED, PUBLISHED_RESTRICTED, PUBLISHED_PUBLIC};
        PUBLISHED_RESTRICTED.allowedDestinationStates = new JobAdvertisementStatus[]{CANCELLED, PUBLISHED_PUBLIC};
        PUBLISHED_PUBLIC.allowedDestinationStates = new JobAdvertisementStatus[]{CANCELLED, ARCHIVED};
        CANCELLED.allowedDestinationStates = new JobAdvertisementStatus[]{};
        ARCHIVED.allowedDestinationStates = new JobAdvertisementStatus[]{};
    }

    private JobAdvertisementStatus[] allowedDestinationStates;

    public JobAdvertisementStatus validateTransitionTo(JobAdvertisementStatus destination) {
        if (!Arrays.asList(allowedDestinationStates).contains(destination)) {
            throw new IllegalJobAdvertisementStatusTransitionException(this, destination);
        }
        return destination;
    }

    public boolean isInAnyStates(JobAdvertisementStatus ... status) {
        return (status != null) && Arrays.asList(status).contains(this);
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
