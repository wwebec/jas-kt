package ch.admin.seco.jobs.services.jobadservice.application.apiuser.dto;

import javax.validation.constraints.NotNull;

public class UpdateStatusApiUserDto {

    @NotNull
    private Boolean active;

    protected UpdateStatusApiUserDto() {
        // For reflection libs
    }

    public UpdateStatusApiUserDto(Boolean active) {
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
