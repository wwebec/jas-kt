package ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.read;

import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Arrays;

@ValidWorkingTimeRange
public class JobAdvertisementSearchRequest {
    private String language;
    @Valid
    private ProfessionCode[] professionCodes;
    private String[] keywords;
    private String[] communalCodes;
    private String[] regionCodes;
    private String[] cantonCodes;
    @Range(min = 0, max = 100)
    private Integer workloadPercentageMin;
    @Range(min = 0, max = 100)
    private Integer workloadPercentageMax;
    private Boolean permanent;
    private String companyName;
    @Min(1)
    @Max(60)
    private Integer onlineSince;
    private Boolean displayRestricted;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public ProfessionCode[] getProfessionCodes() {
        return professionCodes;
    }

    public void setProfessionCodes(ProfessionCode[] professionCodes) {
        this.professionCodes = professionCodes;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }

    public String[] getCommunalCodes() {
        return communalCodes;
    }

    public void setCommunalCodes(String[] communalCodes) {
        this.communalCodes = communalCodes;
    }

    public String[] getRegionCodes() {
        return regionCodes;
    }

    public void setRegionCodes(String[] regionCodes) {
        this.regionCodes = regionCodes;
    }

    public String[] getCantonCodes() {
        return cantonCodes;
    }

    public void setCantonCodes(String[] cantonCodes) {
        this.cantonCodes = cantonCodes;
    }

    public Integer getWorkloadPercentageMin() {
        return workloadPercentageMin;
    }

    public void setWorkloadPercentageMin(Integer workloadPercentageMin) {
        this.workloadPercentageMin = workloadPercentageMin;
    }

    public Integer getWorkloadPercentageMax() {
        return workloadPercentageMax;
    }

    public void setWorkloadPercentageMax(Integer workloadPercentageMax) {
        this.workloadPercentageMax = workloadPercentageMax;
    }

    public Boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(Boolean permanent) {
        this.permanent = permanent;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getOnlineSince() {
        return onlineSince;
    }

    public void setOnlineSince(Integer onlineSince) {
        this.onlineSince = onlineSince;
    }

    public Boolean getDisplayRestricted() {
        return displayRestricted;
    }

    public void setDisplayRestricted(Boolean displayRestricted) {
        this.displayRestricted = displayRestricted;
    }

    @Override
    public String toString() {
        return "JobAdvertisementSearchRequest{" +
                "language='" + language + '\'' +
                ", professionCodes=" + Arrays.toString(professionCodes) +
                ", keywords=" + Arrays.toString(keywords) +
                ", communalCodes=" + Arrays.toString(communalCodes) +
                ", regionCodes=" + Arrays.toString(regionCodes) +
                ", cantonCodes=" + Arrays.toString(cantonCodes) +
                ", workloadPercentageMin=" + workloadPercentageMin +
                ", workloadPercentageMax=" + workloadPercentageMax +
                ", permanent=" + permanent +
                ", companyName='" + companyName + '\'' +
                ", onlineSince=" + onlineSince +
                ", displayRestricted=" + displayRestricted +
                '}';
    }
}
