package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.profession;

import ch.admin.seco.jobs.services.jobadservice.application.ProfessionService;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.Profession;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionCodeType;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultProfessionService implements ProfessionService {

    private final OccupationLabelApiClient occupationLabelApiClient;

    @Autowired
    public DefaultProfessionService(OccupationLabelApiClient occupationLabelApiClient) {
        this.occupationLabelApiClient = occupationLabelApiClient;
    }

    @Override
    public Profession findByAvamCode(String avamCode) {
        OccupationLabelMappingResource resource = occupationLabelApiClient.getOccupationMapping(ProfessionCodeType.AVAM.toString(), avamCode);
        if (resource != null) {
            return new Profession(
                    new ProfessionId(resource.getAvamCode()),
                    resource.getAvamCode(),
                    resource.getSbn3Code(),
                    resource.getSbn5Code(),
                    resource.getBfsCode(),
                    resource.getDescription()
            );
        }
        return null;
    }

}
