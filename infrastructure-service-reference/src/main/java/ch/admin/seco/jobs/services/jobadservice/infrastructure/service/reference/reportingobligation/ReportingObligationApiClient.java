package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.reportingobligation;

import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionCodeType;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "reporting-obligations", fallback = ReportingObligationApiClientFallback.class, decode404 = true)
public interface ReportingObligationApiClient {

    @GetMapping(value = "/reporting-obligations/check-by-canton/{codeType}/{code}", consumes = "application/json")
    boolean hasReportingObligation(
            @PathVariable("codeType") ProfessionCodeType professionCodeType,
            @PathVariable("code") String professionCode,
            @RequestParam("cantonCode") String cantonCode
    );

}

@Component
class ReportingObligationApiClientFallback implements ReportingObligationApiClient {

    @Override
    public boolean hasReportingObligation(ProfessionCodeType professionCodeType, String professionCode, String cantonCode) {
        return false;
    }

}
