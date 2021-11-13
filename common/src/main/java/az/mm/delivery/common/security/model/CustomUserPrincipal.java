package az.mm.delivery.common.security.model;

import az.mm.delivery.common.enums.UserType;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CustomUserPrincipal extends User {

    private String auth;
    private String tokenType;
    private final String fullName;
    private final UserType userType;

    public CustomUserPrincipal(String username, String fullName, String tokenType, String auth, UserType userType,
                               Collection<? extends GrantedAuthority> authorities) {
        super(username, "", true, true, true, true, authorities);
        this.fullName = fullName;
        this.tokenType = tokenType;
        this.auth = auth;
        this.userType = userType;
    }

    public CustomUserPrincipal(String username, String password, String fullName, boolean enabled, UserType userType,
                               Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, true, true, true, authorities);
        this.fullName = fullName;
        this.userType = userType;
    }

}
