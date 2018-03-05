package ch.admin.seco.jobs.services.jobadservice.domain.profession;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;

public class Profession {

    private ProfessionId id;
    private String avamCode;
    private String sbn3Code;
    private String sbn5Code;
    private String bfsCode;
    private String label;

    protected Profession() {
        // For reflection libs
    }

    public Profession(ProfessionId id) {
        this.id = Condition.notNull(id);
    }

    public Profession(ProfessionId id, String avamCode, String sbn3Code, String sbn5Code, String bfsCode, String label) {
        this(id);
        this.avamCode = avamCode;
        this.sbn3Code = sbn3Code;
        this.sbn5Code = sbn5Code;
        this.bfsCode = bfsCode;
        this.label = label;
    }

    public ProfessionId getId() {
        return id;
    }

    public String getAvamCode() {
        return avamCode;
    }

    public String getSbn3Code() {
        return sbn3Code;
    }

    public String getSbn5Code() {
        return sbn5Code;
    }

    public String getBfsCode() {
        return bfsCode;
    }

    public String getLabel() {
        return label;
    }
}
