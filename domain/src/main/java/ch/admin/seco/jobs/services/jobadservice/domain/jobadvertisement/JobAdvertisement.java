package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import java.time.LocalDate;
import java.util.Set;

public class JobAdvertisement {

    private JobAdvertisementId id;
    private String stellennummerAvam;
    private String stellennummerEgov;
    private String fingerprint;
    private String sourceSystem;
    private String sourceEntryId;
    private String externalViewUrl;
    private String externalApplyUrl;
    private JobAdvertisementStatus status;
    private LocalDate publicationStartDate;
    private LocalDate publicationEndDate;
    private String title;
    private String description;
    private LocalDate employmentStartDate;
    private LocalDate employmentEndDate;
    private Boolean immediately;
    private int workloadPercentageMin;
    private int workloadPercentageMax;
    private Boolean permanent;
    private Integer numberOfJobs; // TODO check if used anywhere outside JobRoom
    private Boolean accessibly; // TODO Add this in JobRoom (Improvement-Issue)
    private Boolean jobSharing; // TODO check if used anywhere outside JobRoom
    private Boolean hasPersonalVehicle; // TODO check if used anywhere outside JobRoom
    private String jobCenterCode;
    private String drivingLicenseLevel;
    private ApplyChannel applyChannel;
    private Company company;
    private Contact contact;
    private Set<Locality> localities;
    private Set<Occupation> occupations;
    private Set<LanguageSkill> languages;

}
