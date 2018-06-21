package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.reportingobligation;

public class ReportingObligationResource {

    private boolean hasReportingObligation;
    private ProfessionCodeResource professionCode;
    private String label;

    protected ReportingObligationResource() {
        // For reflection libs
    }

    public ReportingObligationResource(boolean hasReportingObligation, ProfessionCodeResource professionCode, String label) {
        this.hasReportingObligation = hasReportingObligation;
        this.professionCode = professionCode;
        this.label = label;
    }

    public boolean isHasReportingObligation() {
        return hasReportingObligation;
    }

    public void setHasReportingObligation(boolean hasReportingObligation) {
        this.hasReportingObligation = hasReportingObligation;
    }

    public ProfessionCodeResource getProfessionCode() {
        return professionCode;
    }

    public void setProfessionCode(ProfessionCodeResource professionCode) {
        this.professionCode = professionCode;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "ReportingObligationResource{" +
                "hasReportingObligation=" + hasReportingObligation +
                ", professionCode=" + professionCode +
                ", label='" + label + '\'' +
                '}';
    }
}
