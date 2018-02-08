package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

public class Contact {

    private Salutation salutation;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;

    protected Contact() {
    }

    public Contact(Salutation salutation, String firstName, String lastName, String phone, String email) {
        this.salutation = salutation;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }

    public Salutation getSalutation() {
        return salutation;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

}
