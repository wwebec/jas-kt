package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Before;
import org.junit.Test;

import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class JWTFilterTest {

    private static final String SECRET_KEY = "secret.key";
    private static final String AUTHORITIES_KEY = "auth";

    @Before
    public void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void shouldAuthenticateWithJWT() throws IOException, ServletException {
        JWTFilter jwtFilter = new JWTFilter(new TokenProvider(SECRET_KEY));

        // prepare request
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + createJwtToken("user-1", SECRET_KEY, 10));

        // execute
        jwtFilter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain(mock(Servlet.class), jwtFilter));

        // assert authentication and username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication)
                .describedAs("Authentication is null").isNotNull()
                .describedAs("principal does not match").matches(auth -> "user-1".equals(((User) auth.getPrincipal()).getUsername()));

        // assert user roles
        assertThat(
                authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .contains("ROLE_USER", "ROLE_PRIVATE_EMPLOYMENT_AGENT");
    }

    @Test
    public void shouldFailWithOtherSecretKey() throws IOException, ServletException {
        JWTFilter jwtFilter = new JWTFilter(new TokenProvider(SECRET_KEY));

        // prepare request
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + createJwtToken("user-1", "other-secret", 10));

        // execute
        jwtFilter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain(mock(Servlet.class), jwtFilter));

        // assert authentication and username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication)
                .describedAs("Authentication is not null").isNull();
    }

    @Test
    public void shouldFailWithExpiredToken() throws IOException, ServletException {
        JWTFilter jwtFilter = new JWTFilter(new TokenProvider(SECRET_KEY));

        // prepare request
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + createJwtToken("user-1", SECRET_KEY, -10));

        // execute
        jwtFilter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain(mock(Servlet.class), jwtFilter));

        // assert authentication and username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication)
                .describedAs("Authentication is not null").isNull();
    }

    private String createJwtToken(String subject, String secretKey, long expirationInSeconds) {
        return Jwts.builder()
                .setSubject(subject)
                .claim(AUTHORITIES_KEY, "ROLE_USER,ROLE_PRIVATE_EMPLOYMENT_AGENT")
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .setExpiration(Date.from(Instant.now().plusSeconds(expirationInSeconds)))
                .compact();
    }
}
