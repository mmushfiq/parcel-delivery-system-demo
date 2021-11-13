package az.mm.delivery.identity.security;

import az.mm.delivery.identity.error.exception.InvalidAccessTokenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static az.mm.delivery.identity.common.TestConstants.INVALID_AUTHORIZATION_HEADER;
import static az.mm.delivery.identity.common.TestConstants.VALID_AUTHORIZATION_HEADER;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TokenValidatorTest {

    @Test
    void validateAuthorizationHeader_Should_Return_Success() {
        TokenValidator.validateAuthorizationHeader(VALID_AUTHORIZATION_HEADER);
    }

    @ParameterizedTest
    @MethodSource("authHeaderParams")
    void validateAuthorizationHeader_Should_Throw_InvalidAccessTokenException(String authHeader) {
        assertThrows(InvalidAccessTokenException.class,
                () -> TokenValidator.validateAuthorizationHeader(authHeader));
    }

    static Stream<String> authHeaderParams() {
        return Stream.of(null, INVALID_AUTHORIZATION_HEADER, "Bearer ");
    }

}