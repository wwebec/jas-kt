package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.profession;

public class OccupationLabelMappingResource {

    private String id;
    private String bfsCode;
    private String avamCode;
    private String sbn3Code;
    private String sbn5Code;
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBfsCode() {
        return bfsCode;
    }

    public void setBfsCode(String bfsCode) {
        this.bfsCode = bfsCode;
    }

    public String getAvamCode() {
        return avamCode;
    }

    public void setAvamCode(String avamCode) {
        this.avamCode = avamCode;
    }

    public String getSbn3Code() {
        return sbn3Code;
    }

    public void setSbn3Code(String sbn3Code) {
        this.sbn3Code = sbn3Code;
    }

    public String getSbn5Code() {
        return sbn5Code;
    }

    public void setSbn5Code(String sbn5Code) {
        this.sbn5Code = sbn5Code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
