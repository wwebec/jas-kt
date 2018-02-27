package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference;

import ch.admin.seco.jobs.services.jobadservice.application.ReportingObligationService;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionCodeType;
import feign.hystrix.FallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "reporting-obligations", fallback = ReportingObligationApiClientFactory.class, decode404 = true)
public interface ReportingObligationApiClient extends ReportingObligationService {

    @Override
    @GetMapping(value = "/api/reporting-obligations/{type}/{code}/exist", consumes = "application/json")
    boolean hasReportingObligation(@PathVariable("type") ProfessionCodeType professionCodeType, @PathVariable("code") String professionCode, @RequestParam String cantonCode);

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
