package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.security.jwt;

import ch.admin.seco.jobs.services.jobadservice.application.security.CurrentUser;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.security.UserDetailsToCurrentUserAdapter;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);
    private String secretKey;

    public TokenProvider(String secretKey) {
        this.secretKey = secretKey;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(readClaimKey(claims, JWTClaimKey.auth, "").split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        CurrentUser currentUser = new CurrentUser(
                readClaimKey(claims, JWTClaimKey.userId, null),
                readClaimKey(claims, JWTClaimKey.extId, null),
                readClaimKey(claims, JWTClaimKey.companyId, null),
                readClaimKey(claims, JWTClaimKey.firstName, null),
                readClaimKey(claims, JWTClaimKey.lastName, null),
                readClaimKey(claims, JWTClaimKey.email, null),
                authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet())
        );

        UserDetailsToCurrentUserAdapter principal = new UserDetailsToCurrentUserAdapter(
                claims.getSubject(),
                "",
                currentUser,
                authorities
        );

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }

    private String readClaimKey(Claims claims, JWTClaimKey key, String defaultValue) {
        Object value = claims.get(key.name());
        return (value == null) ? defaultValue : value.toString();
    }
}
