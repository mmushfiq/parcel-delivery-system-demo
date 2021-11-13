package az.mm.delivery.common.security;

import az.mm.delivery.common.enums.UserType;
import az.mm.delivery.common.error.exception.CommonAuthException;
import az.mm.delivery.common.security.model.CustomUserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {

    @Value("${application.security.authentication.jwt.base64-secret:}")
    private String base64Secret;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public <T extends CommonAuthException> void validateToken(String token, Supplier<T> exceptionSupplier) {
        if (!isValidToken(token))
            throw exceptionSupplier.get();
    }

    public boolean isValidToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.warn("Invalid JWT signature");
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            log.warn("JWT token compact of handler are invalid");
        } catch (JwtException e) {
            log.warn("Invalid JWT token");
        }
        return false;
    }

    public Authentication parseAuthentication(String authToken) {
        Claims claims = extractClaim(authToken);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(TokenKey.AUTHORITIES).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = getPrincipal(claims, authorities);

        return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
    }

    private Claims extractClaim(String authToken) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(authToken)
                .getBody();
    }

    private CustomUserPrincipal getPrincipal(Claims claims, Collection<? extends GrantedAuthority> authorities) {
        String subject = claims.getSubject();
        String auth = claims.get(TokenKey.AUTHORITIES, String.class);
        String tokenType = claims.get(TokenKey.TOKEN_TYPE, String.class);
        String fullName = claims.get(TokenKey.FULL_NAME, String.class);
        UserType userType = UserType.valueOf(claims.get(TokenKey.USER_TYPE, String.class));
        return new CustomUserPrincipal(subject, fullName, tokenType, auth, userType, authorities);
    }

}