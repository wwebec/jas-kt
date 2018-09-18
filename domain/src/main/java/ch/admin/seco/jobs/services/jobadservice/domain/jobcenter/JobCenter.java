package ch.admin.seco.jobs.services.jobadservice.domain.jobcenter;

public class JobCenter {

    private String id;
    private String code;
    private String email;
    private String phone;
    private String fax;
    private boolean showContactDetailsToPublic;
    private JobCenterAddress address;

    public JobCenter() {
        // For reflection libs
    }

    public JobCenter(String id, String code, String email, String phone, String fax, boolean showContactDetailsToPublic, JobCenterAddress address) {
        this.id = id;
        this.code = code;
        this.email = email;
        this.phone = phone;
        this.fax = fax;
        this.showContactDetailsToPublic = showContactDetailsToPublic;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public JobCenter setId(String id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public JobCenter setCode(String code) {
        this.code = code;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public JobCenter setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public JobCenter setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getFax() {
        return fax;
    }

    public JobCenter setFax(String fax) {
        this.fax = fax;
        return this;
    }

    public boolean isShowContactDetailsToPublic() {
        return showContactDetailsToPublic;
    }

    public JobCenter setShowContactDetailsToPublic(boolean showContactDetailsToPublic) {
        this.showContactDetailsToPublic = showContactDetailsToPublic;
        return this;
    }

    public JobCenterAddress getAddress() {
        return address;
    }

    public JobCenter setAddress(JobCenterAddress address) {
        this.address = address;
        return this;
    }
}
