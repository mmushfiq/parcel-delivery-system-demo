package az.mm.delivery.identity.service;

import az.mm.delivery.common.security.TokenProvider;
import az.mm.delivery.identity.error.exception.InvalidAccessTokenException;
import az.mm.delivery.identity.error.exception.InvalidRefreshTokenException;
import az.mm.delivery.identity.model.SigninRequest;
import az.mm.delivery.identity.model.TokenPair;
import az.mm.delivery.identity.repository.RedisRepository;
import az.mm.delivery.identity.security.jwt.TokenCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static az.mm.delivery.identity.common.TestConstants.AUTHENTICATION;
import static az.mm.delivery.identity.common.TestConstants.INVALID_USER_LOGIN;
import static az.mm.delivery.identity.common.TestConstants.NEW_TOKEN_PAIR;
import static az.mm.delivery.identity.common.TestConstants.TOKEN_PAIR;
import static az.mm.delivery.identity.common.TestConstants.VALID_ACCESS_TOKEN;
import static az.mm.delivery.identity.common.TestConstants.VALID_AUTHORIZATION_HEADER;
import static az.mm.delivery.identity.common.TestConstants.VALID_AUTHORIZATION_HEADER_WITH_INVALID_TOKEN;
import static az.mm.delivery.identity.common.TestConstants.VALID_REFRESH_TOKEN;
import static az.mm.delivery.identity.common.TestConstants.VALID_USER_LOGIN;
import static az.mm.delivery.identity.common.TestUtil.createSecurityContext;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenCreator tokenCreator;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private RedisRepository redisRepository;

    @InjectMocks
    private AuthenticationService authenticationService;


    /*** AuthenticationService -> signin() ***/

    @Test
    void signin_Should_Return_Success() {

        var authToken = new UsernamePasswordAuthenticationToken(
                VALID_USER_LOGIN.getUsername(),
                VALID_USER_LOGIN.getPassword());

        given(authenticationManager.authenticate(authToken)).willReturn(AUTHENTICATION);
        given(tokenCreator.createTokenPair(AUTHENTICATION)).willReturn(TOKEN_PAIR);
        willDoNothing().given(redisRepository).save(AUTHENTICATION.getName(), TOKEN_PAIR);

        TokenPair actualTokenPair = authenticationService.signin(VALID_USER_LOGIN);
        assertNotNull(actualTokenPair);
        assertEquals(TOKEN_PAIR, actualTokenPair);

        then(authenticationManager).should(times(1)).authenticate(authToken);
        then(tokenCreator).should(times(1)).createTokenPair(AUTHENTICATION);
        then(redisRepository).should(times(1)).save(AUTHENTICATION.getName(), TOKEN_PAIR);
    }

    @Test
    void signin_Should_Throw_BadCredentialsException() {

        SigninRequest signinRequest = new SigninRequest(VALID_USER_LOGIN.getUsername(), INVALID_USER_LOGIN.getPassword());

        var authToken = new UsernamePasswordAuthenticationToken(
                VALID_USER_LOGIN.getUsername(),
                INVALID_USER_LOGIN.getPassword());

        given(authenticationManager.authenticate(authToken))
                .willThrow(new BadCredentialsException("Username or password is wrong"));

        BadCredentialsException ex = assertThrows(BadCredentialsException.class,
                () -> authenticationService.signin(signinRequest));
        assertEquals("Username or password is wrong", ex.getMessage());

        then(authenticationManager).should(times(1)).authenticate(authToken);
    }


    /*** AuthenticationService -> signout() ***/

    @Test
    void signout_Should_Return_Success() {

        createSecurityContext(AUTHENTICATION);

        given(redisRepository.read(AUTHENTICATION.getName())).willReturn(TOKEN_PAIR);
        willDoNothing().given(redisRepository).delete(AUTHENTICATION.getName());

        authenticationService.signout(VALID_AUTHORIZATION_HEADER);

        then(redisRepository).should(times(1)).read(AUTHENTICATION.getName());
        then(redisRepository).should(times(1)).delete(AUTHENTICATION.getName());

        SecurityContextHolder.clearContext();
    }

    @Test
    void signout_Should_Throw_InvalidAccessTokenException() {

        createSecurityContext(AUTHENTICATION);

        given(redisRepository.read(AUTHENTICATION.getName())).willReturn(TOKEN_PAIR);

        InvalidAccessTokenException ex = assertThrows(InvalidAccessTokenException.class,
                () -> authenticationService.signout(VALID_AUTHORIZATION_HEADER_WITH_INVALID_TOKEN));

        assertEquals("Invalid access token", ex.getMessage());

        then(redisRepository).should(times(1)).read(AUTHENTICATION.getName());

        SecurityContextHolder.clearContext();
    }


    /*** AuthenticationService -> verify() ***/

    @Test
    void verify_Should_Return_Success() {
        given(tokenProvider.parseAuthentication(VALID_ACCESS_TOKEN)).willReturn(AUTHENTICATION);
        given(redisRepository.read(AUTHENTICATION.getName())).willReturn(TOKEN_PAIR);

        authenticationService.verify(VALID_AUTHORIZATION_HEADER);
        assertEquals(VALID_ACCESS_TOKEN, TOKEN_PAIR.getAccessToken());

        then(tokenProvider).should().parseAuthentication(VALID_ACCESS_TOKEN);
        then(redisRepository).should().read(AUTHENTICATION.getName());
    }

    @Test
    void verify_Should_Throw_InvalidAccessTokenException() {
        String accessToken = "valid_access_token_but_different_redis";
        String authorizationHeader = "Bearer ".concat(accessToken);

        given(tokenProvider.parseAuthentication(accessToken)).willReturn(AUTHENTICATION);
        given(redisRepository.read(AUTHENTICATION.getName())).willReturn(TOKEN_PAIR);

        InvalidAccessTokenException ex = assertThrows(InvalidAccessTokenException.class,
                () -> authenticationService.verify(authorizationHeader));
        assertEquals(401, ex.getCode());
        assertEquals("Invalid access token", ex.getMessage());

        then(tokenProvider).should().parseAuthentication(accessToken);
        then(redisRepository).should().read(AUTHENTICATION.getName());
    }


    /*** AuthenticationService -> refreshToken() ***/

    @Test
    void refreshToken_Should_Return_Success() {
        given(tokenProvider.parseAuthentication(VALID_REFRESH_TOKEN)).willReturn(AUTHENTICATION);
        given(redisRepository.read(AUTHENTICATION.getName())).willReturn(TOKEN_PAIR);
        given(tokenCreator.createTokenPair(AUTHENTICATION)).willReturn(NEW_TOKEN_PAIR);
        willDoNothing().given(redisRepository).update(AUTHENTICATION.getName(), NEW_TOKEN_PAIR);

        authenticationService.refreshToken(VALID_REFRESH_TOKEN);

        then(tokenProvider).should().parseAuthentication(VALID_REFRESH_TOKEN);
        then(redisRepository).should().read(AUTHENTICATION.getName());
        then(tokenCreator).should().createTokenPair(AUTHENTICATION);
        then(redisRepository).should().update(AUTHENTICATION.getName(), NEW_TOKEN_PAIR);
    }

    @Test
    void refreshToken_Should_Throw_InvalidRefreshTokenException() {
        String refreshToken = "valid_refresh_token_but_different_redis";

        given(tokenProvider.parseAuthentication(refreshToken)).willReturn(AUTHENTICATION);
        given(redisRepository.read(AUTHENTICATION.getName())).willReturn(TOKEN_PAIR);

        InvalidRefreshTokenException ex = assertThrows(InvalidRefreshTokenException.class,
                () -> authenticationService.refreshToken(refreshToken));
        assertEquals(401, ex.getCode());
        assertEquals("Invalid refresh token", ex.getMessage());

        then(tokenProvider).should().parseAuthentication(refreshToken);
        then(redisRepository).should().read(AUTHENTICATION.getName());
    }

}