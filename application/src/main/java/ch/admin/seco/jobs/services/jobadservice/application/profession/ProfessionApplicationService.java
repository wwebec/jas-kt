package ch.admin.seco.jobs.services.jobadservice.application.profession;

import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionCode;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionCodeType;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionId;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfessionApplicationService {

    private final ProfessionRepository professionRepository;

    @Autowired
    public ProfessionApplicationService(ProfessionRepository professionRepository) {
        this.professionRepository = professionRepository;
    }

    public String findAvamCode(ProfessionId professionId) {
        return professionRepository.findById(professionId)
                .map(profession -> profession.getCodes().stream()
                        .filter(code -> code.getType().equals(ProfessionCodeType.AVAM))
                        .map(ProfessionCode::getCode)
                        .findFirst()
                        .orElse(null)
                )
                .orElse(null);
    }

}
