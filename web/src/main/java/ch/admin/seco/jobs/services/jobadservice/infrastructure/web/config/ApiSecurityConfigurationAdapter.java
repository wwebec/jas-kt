package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.config;

import ch.admin.seco.jobs.services.jobadservice.application.security.CurrentUser;
import ch.admin.seco.jobs.services.jobadservice.application.security.Role;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUser;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUserRepository;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.security.UserDetailsToCurrentUserAdapter;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER - 1)
@EnableConfigurationProperties(JobAdServiceSecurityProperties.class)
public class ApiSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

    private final ApiUserRepository apiUserRepository;

    public ApiSecurityConfigurationAdapter(ApiUserRepository apiUserRepository) {
        this.apiUserRepository = apiUserRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> {
            ApiUser apiUser = apiUserRepository.findByUsername(username);
            if (apiUser == null) {
                throw new UsernameNotFoundException(username);
            }

            CurrentUser currentUser = new CurrentUser(
                    apiUser.getId().getValue(),
                    null,
                    null,
                    "(API)",
                    apiUser.getCompanyName(),
                    apiUser.getCompanyEmail(),
                    Collections.singleton(Role.API.getValue())
            );

            return new UserDetailsToCurrentUserAdapter(
                    apiUser,
                    currentUser,
                    Collections.singleton(new SimpleGrantedAuthority(Role.API.getValue()))
            );
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/api/public/**")
                .httpBasic().realmName("public-api")
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/public/**").hasAuthority(Role.API.getValue());
    }

}
