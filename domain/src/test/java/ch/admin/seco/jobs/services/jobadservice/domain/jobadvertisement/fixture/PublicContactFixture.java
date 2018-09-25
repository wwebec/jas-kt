package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.PublicContact.Builder;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Salutation.MR;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;

public class PublicContactFixture {

    public static Builder of(JobAdvertisementId id) {
        return testPublicContact()
                .setEmail(String.format("mail-%s@mail.com", id.getValue()))
                .setPhone(String.format("+41 %s", id.getValue()))
                .setFirstName(String.format("first-name-%s", id.getValue()))
                .setLastName(String.format("last-name-%s", id.getValue()));
    }

    public static Builder testPublicContactEmpty() {
        return new Builder();
    }

    public static Builder testPublicContact() {
        return testPublicContactEmpty()
                .setSalutation(MR)
                .setFirstName("firstName")
                .setLastName("lastName")
                .setPhone("phone")
                .setEmail("email");
    }
}