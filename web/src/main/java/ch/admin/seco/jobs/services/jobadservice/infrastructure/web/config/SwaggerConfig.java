package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.config;

import static java.util.Collections.singletonList;
import static springfox.documentation.builders.PathSelectors.regex;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.base.Predicate;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationCodeGrant;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.TokenEndpoint;
import springfox.documentation.service.TokenRequestEndpoint;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.ProfileRegistry;

@Configuration
@EnableSwagger2
class SwaggerConfig {

    @Value("${management.endpoints.web.base-path}")
    String managementContextPath;

    @Configuration
    @Profile("!" + ProfileRegistry.OAUTH2)
    public class JWTSwaggerConfig {

        @Bean
        public Docket publicApi() {
            return new Docket(DocumentationType.SWAGGER_2)
                    .groupName("public-api")
                    .select()
                    .paths(regex("/api/.*"))
                    .build()
                    .directModelSubstitute(LocalDate.class, java.sql.Date.class)
                    .directModelSubstitute(LocalDateTime.class, java.util.Date.class)
                    .securityContexts(Collections.singletonList(securityContext()))
                    .securitySchemes(Collections.singletonList(apiKey()));
        }

        @Bean
        public Docket managementApi() {
            return new Docket(DocumentationType.SWAGGER_2)
                    .groupName("manage-api")
                    .select()
                    .paths(regex(managementContextPath + ".*"))
                    .build()
                    .ignoredParameterTypes(Map.class)
                    .securityContexts(Collections.singletonList(securityContext()))
                    .securitySchemes(Collections.singletonList(apiKey()));
        }

        List<SecurityReference> defaultAuth() {
            AuthorizationScope authorizationScope
                    = new AuthorizationScope("global", "accessEverything");
            AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
            authorizationScopes[0] = authorizationScope;
            return Collections.singletonList(
                    new SecurityReference("JWT", authorizationScopes));
        }

        private ApiKey apiKey() {
            return new ApiKey("JWT", "Authorization", "header");
        }

        private SecurityContext securityContext() {
            return springfox.documentation.spi.service.contexts.SecurityContext.builder()
                    .securityReferences(defaultAuth())
                    .build();
        }
    }

    @Configuration
    @Profile(ProfileRegistry.OAUTH2)
    public class OAuth2SwaggerConfig {
        private static final String OAUTH2 = "OAuth2";
        private final OAuth2ClientProperties oAuth2ClientProperties;

        @Value("${security.oauth2.authorization.realm}")
        private String realm;

        @Value("${security.oauth2.client.user-authorization-uri}")
        private String authorization_endpoint;

        @Value("${security.oauth2.client.access-token-uri}")
        private String token_endpoint;

        public OAuth2SwaggerConfig(OAuth2ClientProperties oAuth2ClientProperties) {
            this.oAuth2ClientProperties = oAuth2ClientProperties;
        }

        @Bean
        public Docket publicApi() {
            return new Docket(DocumentationType.SWAGGER_2)
                    .groupName("public-api")
                    .select()
                    .paths(apiPaths())
                    .build()
                    .directModelSubstitute(LocalDate.class, java.sql.Date.class)
                    .directModelSubstitute(LocalDateTime.class, java.util.Date.class)
                    .securitySchemes(singletonList(oauth()))
                    .securityContexts(singletonList(securityContext()));
        }

        @Bean
        public Docket managementApi() {
            return new Docket(DocumentationType.SWAGGER_2)
                    .groupName("manage-api")
                    .select()
                    .paths(regex(managementContextPath + ".*"))
                    .build()
                    .ignoredParameterTypes(Map.class)
                    .securitySchemes(singletonList(oauth()))
                    .securityContexts(singletonList(securityContext()));
        }

        @Bean
        public SecurityConfiguration securityInfo() {
            return SecurityConfigurationBuilder.builder()
                    .realm(realm)
                    .clientId(oAuth2ClientProperties.getClientId())
                    .clientSecret(oAuth2ClientProperties.getClientSecret())
                    .build();
        }

        @Bean
        List<GrantType> grantTypes() {
            List<GrantType> grantTypes = new ArrayList<>();
            TokenRequestEndpoint tokenRequestEndpoint = new TokenRequestEndpoint(
                    authorization_endpoint,
                    oAuth2ClientProperties.getClientId(),
                    oAuth2ClientProperties.getClientSecret());
            TokenEndpoint tokenEndpoint = new TokenEndpoint(token_endpoint, "token");
            grantTypes.add(new AuthorizationCodeGrant(tokenRequestEndpoint, tokenEndpoint));
            return grantTypes;
        }

        @Bean
        SecurityScheme oauth() {
            return new OAuthBuilder()
                    .name(OAUTH2)
                    .grantTypes(grantTypes())
                    .build();
        }

        private SecurityContext securityContext() {
            return SecurityContext.builder()
                    .forPaths(regex(".*"))
                    .securityReferences(singletonList(SecurityReference.builder()
                            .reference(OAUTH2)
                            .scopes(new AuthorizationScope[0])
                            .build()))
                    .build();
        }

        private Predicate<String> apiPaths() {
            return regex("/api/.*");
        }
    }
}
