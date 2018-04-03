package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.PublicContact;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Salutation;

public class PublicContactDto {

    @NotNull
    private Salutation salutation;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String phone;

    @NotBlank
    private String email;

    protected PublicContactDto() {
        // For reflection libs
    }

    public PublicContactDto(Salutation salutation, String firstName, String lastName, String phone, String email) {
        this.salutation = salutation;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }

    public static PublicContactDto toDto(PublicContact publicContact) {
        if (publicContact == null) {
            return null;
        }
        PublicContactDto publicContactDto = new PublicContactDto();
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
