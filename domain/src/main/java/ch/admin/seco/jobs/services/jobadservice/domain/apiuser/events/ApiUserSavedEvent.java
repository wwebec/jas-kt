package ch.admin.seco.jobs.services.jobadservice.domain.apiuser.events;

import static ch.admin.seco.jobs.services.jobadservice.domain.apiuser.events.ApiUserEvents.API_USER_SAVED;

import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUser;

public class ApiUserSavedEvent extends ApiUserEvent {

	public ApiUserSavedEvent(ApiUser apiUser) {
		super(API_USER_SAVED, apiUser);
	}
}
