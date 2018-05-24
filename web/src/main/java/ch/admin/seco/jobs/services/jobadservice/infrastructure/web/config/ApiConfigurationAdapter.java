package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.config;

import ch.admin.seco.jobs.services.jobadservice.application.security.CurrentUser;
import ch.admin.seco.jobs.services.jobadservice.application.security.Role;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUser;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUserRepository;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.security.UserDetailsToCurrentUserAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import java.util.Collections;

@Configuration
@Order(1)
public class ApiConfigurationAdapter extends WebSecurityConfigurerAdapter {

    private final ApiUserRepository apiUserRepository;

    public ApiConfigurationAdapter(ApiUserRepository apiUserRepository) {
        this.apiUserRepository = apiUserRepository;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> {
            ApiUser apiUser = apiUserRepository.findByUsername(username);
            if (apiUser != null) {
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
                        Collections.singleton(new SimpleGrantedAuthority(Role.API.getValue())));
            }
            return null;
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .authorizeRequests()
                .antMatchers("/api/jobAdvertisements/api-legacy/**").hasAuthority(Role.API.getValue())
                .antMatchers("/api/jobAdvertisements/api/**").hasAuthority(Role.API.getValue());
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
        entryPoint.setRealmName("admin realm");
        return entryPoint;
    }

}
