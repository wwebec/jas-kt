package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class MicroserviceSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
            .antMatchers(HttpMethod.OPTIONS, "/**")
            .antMatchers("/app/**/*.{js,html}")
            .antMatchers("/swagger-resources/**")
            .antMatchers("/test/**")
            .antMatchers("/h2-console/**");
    }

    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        super.configure(http);
        http
            .csrf()
            .disable()
            .exceptionHandling()
        .and()
            .headers()
            .frameOptions()
            .disable()
//        .and()
//            .sessionManagement()
//            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests()
            .antMatchers("/api/**").authenticated()
            .antMatchers("/management/info").permitAll()
            .antMatchers("/management/health").permitAll()
            .antMatchers("/management/**").authenticated()
            .antMatchers("/swagger-resources/configuration/ui").permitAll()
            .antMatchers("/swagger-ui.html").authenticated();
        // @formatter:on
    }
}
