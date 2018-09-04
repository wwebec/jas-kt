package ch.admin.seco.jobs.services.jobadservice.domain.apiuser.events;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventType;

public enum ApiUserEvents {
	API_USER_CREATED(new DomainEventType("API_USER_CREATED")),
	API_USER_UPDATED_DETAILS(new DomainEventType("API_USER_UPDATED_DETAILS")),
	API_USER_UPDATED_STATUS(new DomainEventType("API_USER_UPDATED_STATUS")),
	API_USER_UPDATED_PASSWORD(new DomainEventType("API_USER_UPDATED_PASSWORD")),
	API_USER_UPDATED_LAST_ACCESS_DATE(new DomainEventType("API_USER_UPDATED_LAST_ACCESS_DATE"));

	private DomainEventType domainEventType;

	ApiUserEvents(DomainEventType domainEventType) {
		this.domainEventType = domainEventType;
	}

	public DomainEventType getDomainEventType() {
		return domainEventType;
	}
}
