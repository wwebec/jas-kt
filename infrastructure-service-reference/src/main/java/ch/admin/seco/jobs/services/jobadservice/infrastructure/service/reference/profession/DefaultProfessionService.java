package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.profession;

import ch.admin.seco.jobs.services.jobadservice.application.ProfessionService;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.Profession;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionCode;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionCodeType;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultProfessionService implements ProfessionService {

    private final OccupationLabelApiClient occupationLabelApiClient;

    @Autowired
    public DefaultProfessionService(OccupationLabelApiClient occupationLabelApiClient) {
        this.occupationLabelApiClient = occupationLabelApiClient;
    }

    public Profession findById(ProfessionId professionId) {
        String avamCode = professionId.getValue();
        return findByAvamCode(avamCode);
    }

    public Profession findByAvamCode(String avamCode) {
        OccupationLabelMappingResource resource = occupationLabelApiClient.getOccupationMapping(ProfessionCodeType.AVAM.toString(), avamCode);
        if (resource != null) {
            List<ProfessionCode> codes = new ArrayList<>();
            codes.add(new ProfessionCode(ProfessionCodeType.SBN3, resource.getSbn3Code()));
            codes.add(new ProfessionCode(ProfessionCodeType.SBN5, resource.getSbn5Code()));
            codes.add(new ProfessionCode(ProfessionCodeType.BFS, resource.getBfsCode()));
            codes.add(new ProfessionCode(ProfessionCodeType.AVAM, resource.getAvamCode()));
            return new Profession(new ProfessionId(resource.getAvamCode()), codes);
        }
        return null;
    }

}
