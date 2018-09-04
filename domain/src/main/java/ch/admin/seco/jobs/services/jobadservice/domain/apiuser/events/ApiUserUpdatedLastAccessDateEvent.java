package ch.admin.seco.jobs.services.jobadservice.domain.apiuser.events;

import static ch.admin.seco.jobs.services.jobadservice.domain.apiuser.events.ApiUserEvents.API_USER_UPDATED_LAST_ACCESS_DATE;

import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUser;

public class ApiUserUpdatedLastAccessDateEvent extends ApiUserEvent {
    public ApiUserUpdatedLastAccessDateEvent(ApiUser apiUser) {
        super(API_USER_UPDATED_LAST_ACCESS_DATE, apiUser);
    }
}
