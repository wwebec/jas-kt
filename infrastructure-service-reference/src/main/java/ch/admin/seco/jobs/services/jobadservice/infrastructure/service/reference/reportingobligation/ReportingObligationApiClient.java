package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.reportingobligation;

import feign.hystrix.FallbackFactory;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionCodeType;

@FeignClient(name = "reporting-obligations", fallback = ReportingObligationApiClientFactory.class, decode404 = true)
public interface ReportingObligationApiClient {

    @GetMapping(value = "/reporting-obligations/check-by-canton/{codeType}/{code}", consumes = "application/json")
    boolean hasReportingObligation(
            @PathVariable("type") ProfessionCodeType professionCodeType,
            @PathVariable("code") String professionCode,
            @RequestParam("cantonCode") String cantonCode
    );
}

@Component
class ReportingObligationApiClientFactory implements FallbackFactory<ReportingObligationApiClient> {

    @Override
    public ReportingObligationApiClient create(Throwable cause) {
        return new ReportingObligationApiClient() {
            @Override
            public boolean hasReportingObligation(ProfessionCodeType professionCodeType, String professionCode, String cantonCode) {
                return false;
            }
        };
    }
}
