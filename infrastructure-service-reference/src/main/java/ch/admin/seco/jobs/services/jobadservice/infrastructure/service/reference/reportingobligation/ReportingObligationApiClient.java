package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.reportingobligation;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionCodeType;

@FeignClient(name = "referenceservice", url = "${feign.referenceservice.url:}", fallback = ReportingObligationApiClientFallback.class, decode404 = true)
public interface ReportingObligationApiClient {

    @GetMapping(value = "/api/reporting-obligations/check-by-canton/{codeType}/{code}", consumes = "application/json")
    ReportingObligationResource hasReportingObligation(
            @PathVariable("codeType") ProfessionCodeType professionCodeType,
            @PathVariable("code") String professionCode,
            @RequestParam("cantonCode") String cantonCode
    );

}

@Component
class ReportingObligationApiClientFallback implements ReportingObligationApiClient {

    @Override
    public ReportingObligationResource hasReportingObligation(ProfessionCodeType professionCodeType, String professionCode, String cantonCode) {
        return new ReportingObligationResource(
                true,
                null,
                null
        );
    }

}
