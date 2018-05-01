package ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.read;

import javax.validation.constraints.NotNull;

public class ProfessionCode {
    @NotNull
    private ProfessionCodeType type;
    @NotNull
    private String value;

    protected ProfessionCode() {
    }

    public ProfessionCode(@NotNull ProfessionCodeType type, @NotNull String value) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ProfessionCodeType getType() {
        return type;
    }

    public void setType(ProfessionCodeType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ProfessionCode{" +
                "value=" + value +
                ", type=" + type +
                '}';
    }
}
