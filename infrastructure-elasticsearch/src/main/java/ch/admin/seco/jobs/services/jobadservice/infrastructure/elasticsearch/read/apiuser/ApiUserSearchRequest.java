package ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.read.apiuser;

public class ApiUserSearchRequest {

	private String query;

	protected ApiUserSearchRequest() {
		// For reflection libs
	}

	public ApiUserSearchRequest(String query) {
		this.query = query;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
}
