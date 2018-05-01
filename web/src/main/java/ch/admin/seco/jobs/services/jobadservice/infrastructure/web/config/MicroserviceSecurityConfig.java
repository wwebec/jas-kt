package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.config;

import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedAuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedPrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import ch.admin.seco.jobs.services.jobadservice.application.ProfileRegistry;
import ch.admin.seco.jobs.services.jobadservice.application.security.AuthoritiesConstants;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.security.jwt.JWTConfigurer;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.security.jwt.JWTSecurityProperties;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.security.jwt.TokenProvider;

@Configuration
@Import(SecurityProblemSupport.class)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MicroserviceSecurityConfig {

    @Configuration
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    @EnableConfigurationProperties(JWTSecurityProperties.class)
    @Profile("!" + ProfileRegistry.OAUTH2)
    public class JWTConfig extends WebSecurityConfigurerAdapter {

        private final JWTSecurityProperties securityProperties;
        private final SecurityProblemSupport problemSupport;

        public JWTConfig(JWTSecurityProperties securityProperties, SecurityProblemSupport problemSupport) {
            this.securityProperties = securityProperties;
            this.problemSupport = problemSupport;
        }

        @Override
        public void configure(WebSecurity web) {
            web.ignoring()
                    .antMatchers(HttpMethod.OPTIONS, "/**")
                    .antMatchers("/swagger-resources/**")
                    .antMatchers("/swagger-ui.html");
        }

        protected void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport)
            .and()
                .headers()
                .frameOptions()
                .disable()
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .anonymous()
            .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/jobAdvertisements/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/jobAdvertisements", "/api/jobAdvertisements/_search", "/api/jobAdvertisements/_count").permitAll()
                .antMatchers("/api/**").authenticated()
                .antMatchers("/management/info").permitAll()
                .antMatchers("/management/health").permitAll()
                .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/swagger-ui.html").permitAll()
            .and()
                .apply(securityConfigurerAdapter());
            // @formatter:on
        }

        private JWTConfigurer securityConfigurerAdapter() {
            return new JWTConfigurer(new TokenProvider(securityProperties.getSecret()));
        }
    }

    @Configuration
    @EnableResourceServer
    @Profile(ProfileRegistry.OAUTH2)
    public class OAuth2Config extends ResourceServerConfigurerAdapter {

        private final ResourceServerProperties resourceServerProperties;

        private final SecurityProblemSupport problemSupport;

        public OAuth2Config(ResourceServerProperties resourceServerProperties,
                SecurityProblemSupport problemSupport) {
            this.resourceServerProperties = resourceServerProperties;
            this.problemSupport = problemSupport;
        }

        @Bean
        @Primary
        public UserInfoTokenServices userInfoTokenServices(PrincipalExtractor principalExtractor, AuthoritiesExtractor authoritiesExtractor) {
            UserInfoTokenServices userInfoTokenServices =
                    new UserInfoTokenServices(resourceServerProperties.getUserInfoUri(), resourceServerProperties.getClientId());

            userInfoTokenServices.setPrincipalExtractor(principalExtractor);
            userInfoTokenServices.setAuthoritiesExtractor(authoritiesExtractor);
            return userInfoTokenServices;
        }

        @Bean
        public PrincipalExtractor principalExtractor() {
            return new FixedPrincipalExtractor();
        }

        @Bean
        public AuthoritiesExtractor authoritiesExtractor() {
            return new FixedAuthoritiesExtractor();
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport)
            .and()
                .headers()
                .frameOptions()
                .disable()
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .anonymous()
            .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/jobAdvertisement").permitAll()
                .antMatchers(HttpMethod.POST, "/api/jobAdvertisement", "/api/_search/jobAdvertisement", "/api/_count/jobAdvertisement").permitAll()
                .antMatchers("/api/**").authenticated()
                .antMatchers("/management/info").permitAll()
                .antMatchers("/management/health").permitAll()
                .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN);
            // @formatter:on
        }
    }
}
