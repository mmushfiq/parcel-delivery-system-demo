package az.mm.delivery.identity.security;

import az.mm.delivery.common.constant.CommonConstants.HttpAttribute;

public final class TokenUtil {

    private TokenUtil() {
    }

    public static String extractToken(String authorizationHeader) {
        TokenValidator.validateAuthorizationHeader(authorizationHeader);
        return authorizationHeader.replace(HttpAttribute.BEARER, "").trim();
    }

}
