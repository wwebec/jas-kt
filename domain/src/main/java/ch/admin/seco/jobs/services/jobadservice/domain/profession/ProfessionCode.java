package ch.admin.seco.jobs.services.jobadservice.domain.profession;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;

public class ProfessionCode {

    private ProfessionCodeType type;
    private int code;

    protected ProfessionCode() {
    }

    public ProfessionCode(ProfessionCodeType type, int code) {
        this.type = Condition.notNull(type);
        this.code = code;
    }

    public ProfessionCodeType getType() {
        return type;
    }

    public int getCode() {
        return code;
    }

    public String getTranslationKey() {
        return type.toString() + "." + code;
    }

}
