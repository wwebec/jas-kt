package ch.admin.seco.jobs.services.jobadservice.domain.jobcenter;

public class JobCenter {

    private String id;
    private String code;
    private String email;
    private String phone;
    private String fax;
    private boolean showContactDetailsToPublic;
    private JobCenterAddress address;

    protected JobCenter() {
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

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public boolean isShowContactDetailsToPublic() {
        return showContactDetailsToPublic;
    }

    public void setShowContactDetailsToPublic(boolean showContactDetailsToPublic) {
        this.showContactDetailsToPublic = showContactDetailsToPublic;
    }

    public JobCenterAddress getAddress() {
        return address;
    }

    public void setAddress(JobCenterAddress address) {
        this.address = address;
    }
}
