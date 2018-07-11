package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Contact;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Salutation;

import javax.validation.constraints.*;

public class ApiContactDto {

    @NotNull
    private Salutation salutation;

    @NotBlank
    @Size(max=50)
    private String firstName;

    @NotBlank
    @Size(max=50)
    private String lastName;

    @NotBlank
    @Size(max=20)
    @Pattern(regexp = "[+][0-9]{11,}")
    private String phone;

    @NotBlank
    @Size(max=50)
    @Email
    private String email;

    @NotBlank
    @Size(max=5)
    @Pattern(regexp = "[a-z]{2}")
    private String languageIsoCode;

    protected ApiContactDto() {
        // For reflection libs
    }

    public ApiContactDto(Salutation salutation, String firstName, String lastName, String phone, String email, String languageIsoCode) {
        this.salutation = salutation;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.languageIsoCode = languageIsoCode;
    }

    public Salutation getSalutation() {
        return salutation;
    }

    public void setSalutation(Salutation salutation) {
        this.salutation = salutation;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLanguageIsoCode() {
        return languageIsoCode;
    }

    public void setLanguageIsoCode(String languageIsoCode) {
        this.languageIsoCode = languageIsoCode;
    }

    public static ApiContactDto toDto(Contact contact) {
        if (contact == null) {
            return null;
        }
        ApiContactDto contactDto = new ApiContactDto();
        contactDto.setSalutation(contact.getSalutation());
        contactDto.setFirstName(contact.getFirstName());
        contactDto.setLastName(contact.getLastName());
        contactDto.setPhone(contact.getPhone());
        contactDto.setEmail(contact.getEmail());
        contactDto.setLanguageIsoCode(contact.getLanguage().getLanguage());
        return contactDto;
    }
}
