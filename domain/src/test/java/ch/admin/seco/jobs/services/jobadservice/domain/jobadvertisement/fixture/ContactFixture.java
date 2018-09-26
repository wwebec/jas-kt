package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Salutation.MR;
import static java.lang.String.format;
import static java.util.Locale.GERMAN;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Contact.Builder;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;

public class ContactFixture {

    public static Builder of(JobAdvertisementId id){
        return testContact()
                .setEmail(format("mail-%s@mail.com", id.getValue()))
            .setPhone(format("+41 %s", id.getValue()))
            .setFirstName(format("first-name-%s", id.getValue()))
            .setLastName(format("last-name-%s", id.getValue()));
    }

    public static Builder testContactEmpty() {
        return new Builder();
    }

    public static Builder testContact() {
        return testContactEmpty()
                .setSalutation(MR)
                .setFirstName("firstName")
                .setLastName("lastName")
                .setPhone("phone")
                .setEmail("email")
                .setLanguage(GERMAN);
    }

}
