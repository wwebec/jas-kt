package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.security.jwt;

import static org.springframework.util.StringUtils.arrayToCommaDelimitedString;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import ch.admin.seco.jobs.services.jobadservice.application.security.AuthoritiesConstants;

class JWTTokenGenerator {
    private static final String AUTHORITIES_KEY = "auth";

    static String generateToken(String username, String secretKey, long expirationInSeconds, String... roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim(AUTHORITIES_KEY, arrayToCommaDelimitedString(roles))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .setExpiration(Date.from(Instant.now().plusSeconds(expirationInSeconds)))
                .compact();
    }

    static String generateToken(String username, String secretKey, LocalDate expirationDate, String... roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim(AUTHORITIES_KEY, arrayToCommaDelimitedString(roles))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .setExpiration(Date.from(expirationDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .compact();
    }

    public static void main(String[] args) {
        System.out.println("Bearer " + generateToken("julien", "my-secret-token-to-change-in-production", LocalDate.now().plusYears(1), AuthoritiesConstants.ADMIN, AuthoritiesConstants.PUBLIC_EMPLOYMENT_SERVICE, AuthoritiesConstants.USER));
    }
}
