package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

public class LegacyContactDto {

    private String title;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;

    protected LegacyContactDto() {
        // For reflection libs
    }

    public LegacyContactDto(String title, String firstName, String lastName, String phoneNumber, String email) {
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
