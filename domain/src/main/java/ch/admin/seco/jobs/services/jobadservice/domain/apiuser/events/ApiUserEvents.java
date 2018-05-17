package ch.admin.seco.jobs.services.jobadservice.domain.apiuser.events;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventType;

public enum ApiUserEvents {
	API_USER_SAVED(new DomainEventType("API_USER_SAVED"));

	private DomainEventType domainEventType;

	ApiUserEvents(DomainEventType domainEventType) {
		this.domainEventType = domainEventType;
	}

	public DomainEventType getDomainEventType() {
		return domainEventType;
	}
}
