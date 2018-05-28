package ch.admin.seco.jobs.services.jobadservice.application.apiuser.dto;

import javax.validation.constraints.NotNull;

public class UpdatePasswordApiUserDto {

    @NotNull
    private String password;

    protected UpdatePasswordApiUserDto() {
        // For reflection libs
    }

    public UpdatePasswordApiUserDto(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
