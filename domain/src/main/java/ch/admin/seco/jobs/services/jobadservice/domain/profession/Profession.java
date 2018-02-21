package ch.admin.seco.jobs.services.jobadservice.domain.profession;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.Aggregate;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Profession implements Aggregate<Profession, ProfessionId> {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "ID"))
    @Valid
    private ProfessionId id;

    @ElementCollection
    @CollectionTable(name = "PROFESSION_CODES", joinColumns = @JoinColumn(name = "PROFESSION_ID"))
    @Valid
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
