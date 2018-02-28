package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.reportingobligation;

import ch.admin.seco.jobs.services.jobadservice.application.ReportingObligationService;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionCodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultReportingObligationService implements ReportingObligationService {

    private final ReportingObligationApiClient reportingObligationApiClient;

    @Autowired
    public DefaultReportingObligationService(ReportingObligationApiClient reportingObligationApiClient) {
        this.reportingObligationApiClient = reportingObligationApiClient;
    }

    @Override
    public boolean hasReportingObligation(ProfessionCodeType professionCodeType, String professionCode, String cantonCode) {
        return reportingObligationApiClient.hasReportingObligation(professionCodeType, professionCode, cantonCode);
    }

}
