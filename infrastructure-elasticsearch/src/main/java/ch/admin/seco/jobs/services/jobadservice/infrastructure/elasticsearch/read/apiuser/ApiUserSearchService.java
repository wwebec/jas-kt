package ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.read.apiuser;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ch.admin.seco.jobs.services.jobadservice.application.apiuser.dto.ApiUserDto;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.write.apiuser.ApiUserElasticsearchRepository;

@Service
public class ApiUserSearchService {

	private static final String PATH_CTX = "apiUser.";
	private static final String PATH_ID = "id";
	private static final String PATH_USERNAME = PATH_CTX + "username";
	private static final String PATH_EMAIL = PATH_CTX + "email";
	private static final String PATH_COMPANY_NAME = PATH_CTX + "companyName";
	private static final String PATH_CONTACT_NAME = PATH_CTX + "contactName";
	private static final String PATH_CONTACT_EMAIL = PATH_CTX + "contactEmail";

	private ApiUserElasticsearchRepository apiUserElasticsearchRepository;

	@Autowired
	public ApiUserSearchService(ApiUserElasticsearchRepository apiUserElasticsearchRepository) {
		this.apiUserElasticsearchRepository = apiUserElasticsearchRepository;
	}

	public Page<ApiUserDto> search(ApiUserSearchRequest searchRequest, Pageable pageable) {
		QueryBuilder query = StringUtils.isBlank(searchRequest.getQuery())
				? matchAllQuery()
				: queryStringQuery(searchRequest.getQuery())
				.defaultOperator(Operator.AND)
				.field(PATH_ID)
				.field(PATH_USERNAME)
				.field(PATH_EMAIL)
				.field(PATH_COMPANY_NAME)
				.field(PATH_CONTACT_NAME)
				.field(PATH_CONTACT_EMAIL);

		return apiUserElasticsearchRepository.search(query, pageable)
				.map((document) -> ApiUserDto.toDto(document.getApiUser()));
	}

}
