package az.mm.delivery.identity.security.jwt;

import az.mm.delivery.identity.model.TokenPair;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;

import static az.mm.delivery.identity.common.TestConstants.AUTHENTICATION;
import static az.mm.delivery.identity.common.TestConstants.TOKEN_PROPERTIES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TokenCreatorTest {

    private TokenCreator tokenCreator;

    @BeforeEach
    void beforeEach() {
        byte[] keyBytes = Decoders.BASE64.decode(TOKEN_PROPERTIES.getBase64Secret());
        Key key = Keys.hmacShaKeyFor(keyBytes);
        tokenCreator = new TokenCreator(TOKEN_PROPERTIES);
        ReflectionTestUtils.setField(tokenCreator, "key", key);
    }

    @Test
    void createTokenPair_Should_Return_Success() {
        TokenPair actualTokenPair = tokenCreator.createTokenPair(AUTHENTICATION);
        assertNotNull(actualTokenPair);
        assertThat(actualTokenPair.getAccessToken()).contains(".");
        assertThat(actualTokenPair.getRefreshToken()).contains(".");
    }

}