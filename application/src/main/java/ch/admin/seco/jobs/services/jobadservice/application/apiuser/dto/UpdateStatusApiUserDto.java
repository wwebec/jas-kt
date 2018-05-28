package ch.admin.seco.jobs.services.jobadservice.application.apiuser.dto;

import javax.validation.constraints.NotNull;

public class UpdateStatusApiUserDto {

    @NotNull
    private boolean active;

    protected UpdateStatusApiUserDto() {
        // For reflection libs
    }

    public UpdateStatusApiUserDto(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
