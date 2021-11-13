package az.mm.delivery.identity.security;

import az.mm.delivery.common.constant.CommonConstants.HttpAttribute;
import az.mm.delivery.identity.error.exception.InvalidAccessTokenException;
import org.apache.commons.lang3.StringUtils;

public final class TokenValidator {

    private TokenValidator() {
    }

    public static void validateAuthorizationHeader(String authorizationHeader) {
        if (StringUtils.isBlank(authorizationHeader) ||
                !authorizationHeader.startsWith(HttpAttribute.BEARER) ||
                StringUtils.isBlank(authorizationHeader.replace(HttpAttribute.BEARER, "")))
            throw new InvalidAccessTokenException();
    }

}
