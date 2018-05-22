package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.security.jwt;

import ch.admin.seco.jobs.services.jobadservice.application.security.AuthoritiesConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.springframework.util.StringUtils.arrayToCommaDelimitedString;

class JWTTestTokenGenerator {

    static String generateToken(String username, String secretKey, long expirationInSeconds, String... roles) {
        return Jwts.builder()
                .setSubject(username)
                .setClaims(generateClaims(username, roles))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .setExpiration(Date.from(Instant.now().plusSeconds(expirationInSeconds)))
                .compact();
    }

    static String generateToken(String username, String secretKey, LocalDate expirationDate, String... roles) {
        return Jwts.builder()
                .setSubject(username)
                .setClaims(generateClaims(username, roles))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .setExpiration(Date.from(expirationDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .compact();
    }

    private static Claims generateClaims(String username, String... roles) {
        Claims claims = Jwts.claims();
        claims.setSubject(username);
        claims.put(JWTClaimKey.userId.name(), "userId");
        claims.put(JWTClaimKey.extId.name(), "extId");
        claims.put(JWTClaimKey.companyId.name(), "companyId");
        claims.put(JWTClaimKey.firstName.name(), "firstName");
        claims.put(JWTClaimKey.lastName.name(), "lastName");
        claims.put(JWTClaimKey.email.name(), "email");
        claims.put(JWTClaimKey.auth.name(), arrayToCommaDelimitedString(roles));
        return claims;
    }

    public static void main(String[] args) {
        System.out.println("Bearer " + generateToken("julien", "my-secret-token-to-change-in-production", LocalDate.now().plusYears(1), AuthoritiesConstants.ADMIN, AuthoritiesConstants.PUBLIC_EMPLOYMENT_SERVICE, AuthoritiesConstants.USER));
    }
}
