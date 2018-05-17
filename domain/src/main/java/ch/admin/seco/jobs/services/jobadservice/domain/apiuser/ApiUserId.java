package ch.admin.seco.jobs.services.jobadservice.domain.apiuser;

import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateId;
import ch.admin.seco.jobs.services.jobadservice.core.domain.IdGenerator;

@Embeddable
@Access(AccessType.FIELD)
public class ApiUserId implements AggregateId<ApiUserId> {

	private String value;

	public ApiUserId() {
		this(IdGenerator.timeBasedUUID().toString());
	}

	public ApiUserId(String value) {
		this.value = Condition.notBlank(value);;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (o == null || getClass() != o.getClass()) { return false; }
		ApiUserId apiUserId = (ApiUserId) o;
		return Objects.equals(value, apiUserId.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public String toString() {
		return "ApiUserId{" +
				"value='" + value + '\'' +
				'}';
	}
}
