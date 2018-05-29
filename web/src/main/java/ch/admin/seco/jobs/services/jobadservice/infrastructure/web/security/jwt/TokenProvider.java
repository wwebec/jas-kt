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
                Arrays.stream(claims.get(JWTClaimKey.auth.name()).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        CurrentUser currentUser = new CurrentUser(
                claims.get(JWTClaimKey.userId.name()).toString(),
                claims.get(JWTClaimKey.extId.name()).toString(),
                claims.get(JWTClaimKey.companyId.name()).toString(),
                claims.get(JWTClaimKey.firstName.name()).toString(),
                claims.get(JWTClaimKey.lastName.name()).toString(),
                claims.get(JWTClaimKey.email.name()).toString(),
                authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet())
        );

        UserDetailsToCurrentUserAdapter principal = new UserDetailsToCurrentUserAdapter(
                claims.getSubject(),
                "",
                currentUser,
                authorities
        );

        //User principal = new User(claims.getSubject(), "", authorities);

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
}
