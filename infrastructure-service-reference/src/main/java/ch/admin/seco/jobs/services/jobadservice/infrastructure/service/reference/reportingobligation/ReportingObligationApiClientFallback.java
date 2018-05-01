package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.reportingobligation;

import org.springframework.stereotype.Component;

import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionCodeType;

@Component
public class ReportingObligationApiClientFallback implements ReportingObligationApiClient {

    @Override
    public ReportingObligationResource hasReportingObligation(ProfessionCodeType professionCodeType, String professionCode, String cantonCode) {
        return new ReportingObligationResource(
                true,
                null,
                null
        );
    }

}
