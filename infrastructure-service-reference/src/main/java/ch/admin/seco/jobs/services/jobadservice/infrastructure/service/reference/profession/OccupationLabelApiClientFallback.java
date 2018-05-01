package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.profession;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

@Component
public class OccupationLabelApiClientFallback implements OccupationLabelApiClient {

    @Override
    public OccupationLabelMappingResource getOccupationMapping(@PathVariable("type") String type, @PathVariable("code") String code) {
        return null;
    }

}
