package ch.admin.seco.jobs.services.jobadservice.domain.apiuser.events;

import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUser;

import static ch.admin.seco.jobs.services.jobadservice.domain.apiuser.events.ApiUserEvents.API_USER_CREATED;

public class ApiUserCreatedEvent extends ApiUserEvent {

    public ApiUserCreatedEvent(ApiUser apiUser) {
        super(API_USER_CREATED, apiUser);
    }
}
