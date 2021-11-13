package az.mm.delivery.identity.security.jwt;

import az.mm.delivery.common.security.TokenKey;
import az.mm.delivery.common.security.model.CustomUserPrincipal;
import az.mm.delivery.identity.config.properties.TokenProperties;
import az.mm.delivery.identity.enums.TokenType;
import az.mm.delivery.identity.model.TokenPair;
import az.mm.delivery.identity.util.FormatterUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenCreator {

    private final TokenProperties tokenProperties;
    private Key key;

    @PostConstruct
    private void init() {
        byte[] keyBytes = Decoders.BASE64.decode(tokenProperties.getBase64Secret());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenPair createTokenPair(Authentication authentication) {
        return TokenPair.builder()
                .accessToken(createToken(authentication, TokenType.ACCESS))
                .refreshToken(createToken(authentication, TokenType.REFRESH)).build();
    }

    private String createToken(Authentication authentication, TokenType tokenType) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        CustomUserPrincipal customUserPrincipal = (CustomUserPrincipal) authentication.getPrincipal();

        Long tokenValidityInSeconds = (tokenType == TokenType.ACCESS) ?
                tokenProperties.getAccessTokenValidityInSeconds() : tokenProperties.getRefreshTokenValidityInSeconds();
        LocalDateTime validity = LocalDateTime.now().plusSeconds(tokenValidityInSeconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(TokenKey.AUTHORITIES, authorities)
                .claim(TokenKey.TOKEN_TYPE, tokenType)
                .claim(TokenKey.FULL_NAME, customUserPrincipal.getFullName())
                .claim(TokenKey.USER_TYPE, customUserPrincipal.getUserType())
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(FormatterUtil.convertToUtilDate(validity))
                .compact();
    }

}
