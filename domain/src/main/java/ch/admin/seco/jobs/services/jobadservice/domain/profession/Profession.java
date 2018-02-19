package ch.admin.seco.jobs.services.jobadservice.domain.profession;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.Aggregate;

import java.util.ArrayList;
import java.util.List;

public class Profession implements Aggregate<Profession, ProfessionId> {

    private ProfessionId id;
    private List<ProfessionCode> codes;

    public Profession() {
    }

    public Profession(ProfessionId id) {
        this.id = Condition.notNull(id);
        this.codes = new ArrayList<>();
    }

    public Profession(ProfessionId id, List<ProfessionCode> codes) {
        this(id);
        this.codes = Condition.notNull(codes);
    }

    @Override
    public boolean sameAggregateAs(Profession other) {
        return (other != null) && id.sameValueObjectAs(other.id);
    }

    public ProfessionId getId() {
        return id;
    }

    public List<ProfessionCode> getCodes() {
        return codes;
    }

}
