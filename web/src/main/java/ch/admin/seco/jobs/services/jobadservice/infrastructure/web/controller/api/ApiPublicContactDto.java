package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller.api;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.PublicContact;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Salutation;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils.PhoneNumber;

import javax.validation.constraints.*;

public class ApiPublicContactDto {

    @NotNull
    private Salutation salutation;

    @NotBlank
    @Size(max=50)
    private String firstName;

    @NotBlank
    @Size(max=50)
    private String lastName;

    @PhoneNumber
    private String phone;

    @Size(max=50)
    @Email
    private String email;

    protected ApiPublicContactDto() {
        // For reflection libs
    }

    public ApiPublicContactDto(Salutation salutation, String firstName, String lastName, String phone, String email) {
        this.salutation = salutation;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }

    public static ApiPublicContactDto toDto(PublicContact publicContact) {
        if (publicContact == null) {
            return null;
        }
        ApiPublicContactDto publicContactDto = new ApiPublicContactDto();
        publicContactDto.setSalutation(publicContact.getSalutation());
        publicContactDto.setFirstName(publicContact.getFirstName());
        publicContactDto.setLastName(publicContact.getLastName());
        publicContactDto.setPhone(publicContact.getPhone());
        publicContactDto.setEmail(publicContact.getEmail());
        return publicContactDto;
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
}
