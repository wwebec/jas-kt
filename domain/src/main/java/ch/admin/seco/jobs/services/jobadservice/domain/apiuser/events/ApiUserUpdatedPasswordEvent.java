package ch.admin.seco.jobs.services.jobadservice.domain.apiuser.events;

import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUser;

import static ch.admin.seco.jobs.services.jobadservice.domain.apiuser.events.ApiUserEvents.API_USER_UPDATED_PASSWORD;

public class ApiUserUpdatedPasswordEvent extends ApiUserEvent {

    public ApiUserUpdatedPasswordEvent(ApiUser apiUser) {
        super(API_USER_UPDATED_PASSWORD, apiUser);
    }
}
