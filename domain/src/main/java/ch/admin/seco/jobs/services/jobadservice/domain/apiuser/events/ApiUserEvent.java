package ch.admin.seco.jobs.services.jobadservice.domain.apiuser.events;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUser;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUserId;

public class ApiUserEvent extends DomainEvent<ApiUserId> {

	protected ApiUserId apiUserId;

	public ApiUserEvent(ApiUserEvents apiUserEventType, ApiUser apiUser) {
		super(apiUserEventType.getDomainEventType(), ApiUser.class.getSimpleName());
		this.apiUserId = apiUser.getId();
		additionalAttributes.put("apiUserId", apiUserId.getValue());
	}

	@Override
	public ApiUserId getAggregateId() {
		return apiUserId;
	}
}
