package ch.admin.seco.jobs.services.jobadservice.domain.profession;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.ValueObject;

import javax.persistence.*;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class ProfessionCode implements ValueObject<ProfessionCode> {

    @Enumerated(EnumType.STRING)
    private ProfessionCodeType type;

    private String code;

    protected ProfessionCode() {
    }

    public ProfessionCode(ProfessionCodeType type, String code) {
        this.type = Condition.notNull(type);
        this.code = Condition.notBlank(code);
    }

    public ProfessionCodeType getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public String getTranslationKey() {
        return type.toString() + "." + code;
    }

    @Override
    public boolean sameValueObjectAs(ProfessionCode other) {
        return equals(other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfessionCode that = (ProfessionCode) o;
        return type == that.type &&
                Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, code);
    }

}
