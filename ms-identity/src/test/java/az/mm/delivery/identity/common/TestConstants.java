package az.mm.delivery.identity.common;

import az.mm.delivery.common.enums.UserType;
import az.mm.delivery.common.security.model.CustomUserPrincipal;
import az.mm.delivery.identity.config.properties.TokenProperties;
import az.mm.delivery.identity.model.SigninRequest;
import az.mm.delivery.identity.model.TokenPair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;


public interface TestConstants {

    //CHECKSTYLE:OFF

    String USER_NAME = "mushfiqazeri@gmail.com";
    String PASSWORD = "test1234";
    String FULL_NAME = "Mushfiq Mammadov";

    SigninRequest VALID_USER_LOGIN = new SigninRequest(USER_NAME, PASSWORD);
    SigninRequest INVALID_USER_LOGIN = new SigninRequest("invalid-user", "wrong-password");

    String VALID_ACCESS_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtdXNoZmlxYXplcmlAZ21haWwuY29tIiwiYXV0aCI6IlJPTEVfQURNSU4sYWNjZXB0X29yZGVyLGFkZF9wZXJtaXNzaW9uLGFkZF9yb2xlLGFkZF91c2VyLGFzc2lnbl9jb3VyaWVyLGNhbmNlbF9vcmRlcixjaGFuZ2Vfb3JkZXJfc3RhdHVzLGNoYW5nZV9wYXJjZWxfc3RhdHVzLGNyZWF0ZV9jb3VyaWVyLGRlbGV0ZV9wZXJtaXNzaW9uLGRlbGV0ZV9yb2xlLGRlbGV0ZV91c2VyLHRyYWNrX3BhcmNlbCx1cGRhdGVfb3JkZXIsdXBkYXRlX3BhcmNlbCx1cGRhdGVfcGVybWlzc2lvbix1cGRhdGVfcm9sZSx1cGRhdGVfdXNlcix2aWV3X2NvdXJpZXIsdmlld19vcmRlcix2aWV3X3BhcmNlbCx2aWV3X3Blcm1pc3Npb24sdmlld19yb2xlLHZpZXdfdXNlcix2aWV3X3VzZXJfbGlzdCIsInRva2VuX3R5cGUiOiJBQ0NFU1MiLCJmdWxsX25hbWUiOiJNdXNoZmlxIE1hbW1hZG92IiwidXNlcl90eXBlIjoiQURNSU4iLCJleHAiOjI1ODI3ODQ4NDZ9.QTgRGY4AeJuqY92tU3JykeGTBTJLjod-HoYCcjyXL2y3cusIG6mYdE3N5ofUFOyts-BE_RQuMUdrYv5iXuv2rg";
    String VALID_REFRESH_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtdXNoZmlxYXplcmlAZ21haWwuY29tIiwiYXV0aCI6IlJPTEVfQURNSU4sYWNjZXB0X29yZGVyLGFkZF9wZXJtaXNzaW9uLGFkZF9yb2xlLGFkZF91c2VyLGFzc2lnbl9jb3VyaWVyLGNhbmNlbF9vcmRlcixjaGFuZ2Vfb3JkZXJfc3RhdHVzLGNoYW5nZV9wYXJjZWxfc3RhdHVzLGNyZWF0ZV9jb3VyaWVyLGRlbGV0ZV9wZXJtaXNzaW9uLGRlbGV0ZV9yb2xlLGRlbGV0ZV91c2VyLHRyYWNrX3BhcmNlbCx1cGRhdGVfb3JkZXIsdXBkYXRlX3BhcmNlbCx1cGRhdGVfcGVybWlzc2lvbix1cGRhdGVfcm9sZSx1cGRhdGVfdXNlcix2aWV3X2NvdXJpZXIsdmlld19vcmRlcix2aWV3X3BhcmNlbCx2aWV3X3Blcm1pc3Npb24sdmlld19yb2xlLHZpZXdfdXNlcix2aWV3X3VzZXJfbGlzdCIsInRva2VuX3R5cGUiOiJSRUZSRVNIIiwiZnVsbF9uYW1lIjoiTXVzaGZpcSBNYW1tYWRvdiIsInVzZXJfdHlwZSI6IkFETUlOIiwiZXhwIjozMjEzNTA0ODQ2fQ.tweKZYIHEkq9v5pLZK55-QpSw9vB8fi-syyER7WTUQ9iVShhasqwqXtFipxttMfljUXvpb5B3YRo2Qp7a_QyYw";

    String INVALID_ACCESS_TOKEN = "invalid.access.token";
    String INVALID_REFRESH_TOKEN = "invalid.refresh.token";

    String VALID_AUTHORIZATION_HEADER = "Bearer ".concat(VALID_ACCESS_TOKEN);
    String VALID_AUTHORIZATION_HEADER_WITH_INVALID_TOKEN = "Bearer ".concat(INVALID_ACCESS_TOKEN);
    String INVALID_AUTHORIZATION_HEADER = "Invalid ".concat(INVALID_ACCESS_TOKEN);

    CustomUserPrincipal USER_PRINCIPAL = new CustomUserPrincipal(USER_NAME, PASSWORD, FULL_NAME, true, UserType.ADMIN, Set.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
    Authentication AUTHENTICATION = new UsernamePasswordAuthenticationToken(USER_PRINCIPAL, PASSWORD, USER_PRINCIPAL.getAuthorities());

    TokenPair TOKEN_PAIR = new TokenPair(VALID_ACCESS_TOKEN, VALID_REFRESH_TOKEN);
    TokenPair NEW_TOKEN_PAIR = new TokenPair("new.access.token", "new.refresh.token");

    TokenProperties TOKEN_PROPERTIES = TokenProperties.builder()
            .base64Secret("ZEdocGN5MXBjeTEwWlhOMExYTmxZM0psZEMxclpYa3RabTl5TFhCaGNtTmxiQzFrWld4cGRtVnllUzF6ZVhOMFpXMD0=")
            .accessTokenValidityInSeconds(900L)
            .refreshTokenValidityInSeconds(3600L)
            .build();

    //CHECKSTYLE:ON

}