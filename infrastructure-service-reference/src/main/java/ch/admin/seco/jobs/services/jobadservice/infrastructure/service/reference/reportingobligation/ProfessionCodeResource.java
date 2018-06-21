package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.reportingobligation;

public class ProfessionCodeResource {

    private String code;
    private ProfessionCodeType codeType;

    protected ProfessionCodeResource() {
        // For reflection libs
    }

    public ProfessionCodeResource(String code, ProfessionCodeType codeType) {
        this.code = code;
        this.codeType = codeType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ProfessionCodeType getCodeType() {
        return codeType;
    }

    public void setCodeType(ProfessionCodeType codeType) {
        this.codeType = codeType;
    }

    @Override
    public String toString() {
        return "ProfessionCodeResource{" +
                "code='" + code + '\'' +
                ", codeType=" + codeType +
                '}';
    }
}
