package ch.admin.seco.jobs.services.jobadservice.infrastructure.api.client;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultRequestEnhancer;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;

public class OauthRestTemplateTest {

    @Test
    public void resourceOwnerPasswordAccessPublic() {
        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceDetails.setUsername("admin");
        resourceDetails.setPassword("admin");
        resourceDetails.setClientAuthenticationScheme(AuthenticationScheme.none);
        resourceDetails.setAccessTokenUri("http://dev.job-room.ch:9080/auth/realms/jobroom/protocol/openid-connect/token");
        resourceDetails.setClientId("jobroom-api");
//        resourceDetails.setClientId("job-ad-service");

        DefaultAccessTokenRequest accessTokenRequest = new DefaultAccessTokenRequest();
        accessTokenRequest.set("client_id", "jobroom-api");
        DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext(accessTokenRequest);

        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails, clientContext);
        ResourceOwnerPasswordAccessTokenProvider accessTokenProvider = new ResourceOwnerPasswordAccessTokenProvider();
        DefaultRequestEnhancer defaultRequestEnhancer = new DefaultRequestEnhancer();
        defaultRequestEnhancer.setParameterIncludes(Collections.singletonList("client_id"));
        accessTokenProvider.setTokenRequestEnhancer(defaultRequestEnhancer);
        restTemplate.setAccessTokenProvider(accessTokenProvider);

        final List result = restTemplate.getForObject("http://localhost:8086/api/jobAdvertisement", List.class);

        System.out.println(result);
    }

    @Test
    public void resourceOwnerPasswordAccess() {
        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceDetails.setUsername("admin");
        resourceDetails.setPassword("admin");
        resourceDetails.setAccessTokenUri("http://dev.job-room.ch:9080/auth/realms/jobroom/protocol/openid-connect/token");
        resourceDetails.setClientId("job-ad-service");
        resourceDetails.setClientSecret("fba46e62-44b4-4fd1-b76c-89d8c448148f");

        DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();

        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails, clientContext);

        final List result = restTemplate.getForObject("http://localhost:8086/api/jobAdvertisement", List.class);

        System.out.println(result);
    }

    @Test
    public void clientCredentialsAccess() {
        ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
        resourceDetails.setAccessTokenUri("http://dev.job-room.ch:9080/auth/realms/jobroom/protocol/openid-connect/token");
        resourceDetails.setClientId("internal");
        resourceDetails.setClientSecret("fba46e62-44b4-4fd1-b76c-89d8c448148f");

        DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();

        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails, clientContext);

        final List result = restTemplate.getForObject("http://localhost:8086/api/jobAdvertisement", List.class);

        System.out.println(result);
    }

    @Test
    public void authorizationCodeAccess() {
        AuthorizationCodeResourceDetails resourceDetails = new AuthorizationCodeResourceDetails();
        resourceDetails.setId("oauth2server");
        resourceDetails.setTokenName("oauth_token");
        resourceDetails.setClientId("internal");
        resourceDetails.setClientSecret("fba46e62-44b4-4fd1-b76c-89d8c448148f");
        resourceDetails.setAccessTokenUri("http://dev.job-room.ch:9080/auth/realms/jobroom/protocol/openid-connect/token");
        resourceDetails.setUserAuthorizationUri("http://dev.job-room.ch:9080/auth/realms/jobroom/protocol/openid-connect/auth");
        resourceDetails.setPreEstablishedRedirectUri("http://localhost:8086");
        resourceDetails.setUseCurrentUri(false);

        DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();

        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails, clientContext);

        final List result = restTemplate.getForObject("http://localhost:8086/api/jobAdvertisement", List.class);

        System.out.println(result);
    }

}
