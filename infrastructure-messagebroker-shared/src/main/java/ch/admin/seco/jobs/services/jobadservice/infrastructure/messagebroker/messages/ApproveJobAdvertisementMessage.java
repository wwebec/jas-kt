package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages;

import java.time.LocalDate;
import java.util.List;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Occupation;

public class ApproveJobAdvertisementMessage {

    private String stellennummerEgov;

    private boolean reportingObligation;

    private LocalDate reportingObligationEndDate;

    private List<Occupation> occupations;

    public String getStellennummerEgov() {
        return stellennummerEgov;
    }

    public void setStellennummerEgov(String stellennummerEgov) {
        this.stellennummerEgov = stellennummerEgov;
    }

    public boolean isReportingObligation() {
        return reportingObligation;
    }

    public void setReportingObligation(boolean reportingObligation) {
        this.reportingObligation = reportingObligation;
    }

    public LocalDate getReportingObligationEndDate() {
        return reportingObligationEndDate;
    }

    public void setReportingObligationEndDate(LocalDate reportingObligationEndDate) {
        this.reportingObligationEndDate = reportingObligationEndDate;
    }

    public List<Occupation> getOccupations() {
        return occupations;
    }

    public void setOccupations(List<Occupation> occupations) {
        this.occupations = occupations;
    }
}
