package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.profession;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "localities", fallback = OccupationLabelApiClientFallback.class, decode404 = true)
public interface OccupationLabelApiClient {

    @GetMapping("/occupations/label/mapping/{type}/{code}")
    OccupationLabelMappingResource getOccupationMapping(@PathVariable("type") String type, @PathVariable("code") String code);

}

@Component
class OccupationLabelApiClientFallback implements OccupationLabelApiClient {

    @Override
    public OccupationLabelMappingResource getOccupationMapping(@PathVariable("type") String type, @PathVariable("code") String code) {
        return null;
    }

}
