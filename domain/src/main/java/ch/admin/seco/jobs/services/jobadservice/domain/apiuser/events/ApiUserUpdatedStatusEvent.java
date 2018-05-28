package ch.admin.seco.jobs.services.jobadservice.domain.apiuser.events;

import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUser;

import static ch.admin.seco.jobs.services.jobadservice.domain.apiuser.events.ApiUserEvents.API_USER_UPDATED_STATUS;

public class ApiUserUpdatedStatusEvent extends ApiUserEvent {

    public ApiUserUpdatedStatusEvent(ApiUser apiUser) {
        super(API_USER_UPDATED_STATUS, apiUser);
        additionalAttributes.put("status", apiUser.isActive());
    }
}
