package ch.admin.seco.jobs.services.jobadservice.domain.profession;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;

public class ProfessionCode {

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

}
