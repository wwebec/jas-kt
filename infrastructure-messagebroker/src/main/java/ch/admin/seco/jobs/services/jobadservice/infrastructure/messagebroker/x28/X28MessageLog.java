package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.x28;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class X28MessageLog {

    @Id
    @NotNull
    private String fingerprint;

    @NotNull
    private LocalDate lastMessageDate;

    public X28MessageLog() {
    }

    public X28MessageLog(@NotNull String fingerprint, @NotNull LocalDate lastMessageDate) {
        this.fingerprint = fingerprint;
        this.lastMessageDate = lastMessageDate;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public LocalDate getLastMessageDate() {
        return lastMessageDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        X28MessageLog that = (X28MessageLog) o;
        return Objects.equals(fingerprint, that.fingerprint);
    }

    @Override
    public int hashCode() {

        return Objects.hash(fingerprint);
    }

    @Override
    public String toString() {
        return "X28MessageLog{" +
                "fingerprint='" + fingerprint + '\'' +
                ", lastMessageDate=" + lastMessageDate +
                '}';
    }
}
