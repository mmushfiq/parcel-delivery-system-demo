package az.mm.delivery.identity.security;

import az.mm.delivery.common.enums.UserType;
import az.mm.delivery.common.security.model.CustomUserPrincipal;
import az.mm.delivery.identity.domain.Role;
import az.mm.delivery.identity.domain.User;
import az.mm.delivery.identity.error.exception.DefaultUserRoleNotExistException;
import az.mm.delivery.identity.error.exception.UserNotEnabledException;
import az.mm.delivery.identity.error.exception.UserNotFoundException;
import az.mm.delivery.identity.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static az.mm.delivery.identity.common.TestConstants.USER_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;


    @Test
    void loadUserByUsername_Should_Throw_UserNotFoundException() {
        UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("wrongUserName"));

        assertEquals(401, ex.getCode());
        assertEquals("User is not found!", ex.getMessage());
    }

    @Test
    void loadUserByUsername_Should_Throw_UserNotEnabledException() {

        User user = User.builder()
                .username(USER_NAME)
                .enabled(false)
                .build();

        given(userRepository.findByUsernameEqualsIgnoreCase(USER_NAME)).willReturn(Optional.of(user));

        UserNotEnabledException ex = assertThrows(UserNotEnabledException.class,
                () -> userDetailsService.loadUserByUsername(USER_NAME));

        assertEquals(401, ex.getCode());
        assertEquals("User is not enabled", ex.getMessage());
    }

    @Test
    void loadUserByUsername_Should_Throw_DefaultUserRoleNotExistException() {

        User user = User.builder()
                .username(USER_NAME)
                .enabled(true)
                .roles(Collections.emptySet())
                .build();

        given(userRepository.findByUsernameEqualsIgnoreCase(USER_NAME)).willReturn(Optional.of(user));

        DefaultUserRoleNotExistException ex = assertThrows(DefaultUserRoleNotExistException.class,
                () -> userDetailsService.loadUserByUsername(USER_NAME));

        assertEquals(401, ex.getCode());
        assertEquals("There is no default role of user", ex.getMessage());
    }

    @Test
    void loadUserByUsername_Should_Return_Success() {

        Role role = Role.builder()
                .id(2L)
                .name("USER")
                .description("user")
                .permissions(Collections.emptySet())
                .build();

        String username = "hasanov.ceyhun@gmail.com";
        User user = User.builder()
                .id(2L)
                .username(username)
                .password("test1234")
                .firstName("Ceyhun")
                .lastName("Hasanov")
                .type(UserType.USER)
                .phone("+994555906952")
                .enabled(true)
                .roles(Set.of(role))
                .build();

        given(userRepository.findByUsernameEqualsIgnoreCase(username)).willReturn(Optional.of(user));

        CustomUserPrincipal expectedUserPrincipal = new CustomUserPrincipal(username, "test1234", "Ceyhun Hasanov", true,
                UserType.USER, Set.of(new SimpleGrantedAuthority("ROLE_USER")));

        assertEquals(expectedUserPrincipal, userDetailsService.loadUserByUsername(username));
    }

}