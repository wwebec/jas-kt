package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.reportingobligation;

import ch.admin.seco.jobs.services.jobadservice.application.ReportingObligationService;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionCodeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultReportingObligationService implements ReportingObligationService {
    private static Logger LOG = LoggerFactory.getLogger(DefaultReportingObligationService.class);

    private final ReportingObligationApiClient reportingObligationApiClient;

    @Autowired
    public DefaultReportingObligationService(ReportingObligationApiClient reportingObligationApiClient) {
        this.reportingObligationApiClient = reportingObligationApiClient;
    }

    @Override
    public boolean hasReportingObligation(ProfessionCodeType professionCodeType, String professionCode, String cantonCode) {
        LOG.debug("Check reporting obligation for: {} {} {}", professionCodeType, professionCode, cantonCode);

        ReportingObligationResource resource = reportingObligationApiClient.hasReportingObligation(professionCodeType, professionCode, cantonCode);

        LOG.debug("reporting obligation check result: {}", resource);

        return (resource != null) && resource.isHasReportingObligation();
    }

}
