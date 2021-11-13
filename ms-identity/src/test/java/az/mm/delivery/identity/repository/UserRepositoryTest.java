package az.mm.delivery.identity.repository;

import az.mm.delivery.common.enums.UserType;
import az.mm.delivery.identity.domain.User;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    void loadRepository() {
        assertThat(userRepository).isNotNull();
    }

    @Test
    @Order(2)
    void crudOperation_Should_Return_Success() {

        // insert
        User user = User.builder()
                .username("mushfiqazeri@mail.ru")
                .password("12345678")
                .firstName("Mushfiq")
                .lastName("Mammadov")
                .type(UserType.ADMIN)
                .phone("+994555906952")
                .enabled(true)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        userRepository.saveAndFlush(user);

        // select
        User actualUser = userRepository.findByUsernameEqualsIgnoreCase(user.getUsername()).orElse(null);
        assertThat(actualUser).isNotNull();
        assertEquals(user, actualUser);

        // update
        user.setEnabled(false);
        userRepository.saveAndFlush(user);
        User updatedUser = userRepository.findByUsernameEqualsIgnoreCase(user.getUsername()).orElse(null);
        assertThat(updatedUser).isNotNull();
        assertEquals(false, updatedUser.getEnabled());

        // delete
        userRepository.delete(user);
        User deletedUser = userRepository.findByUsernameEqualsIgnoreCase(user.getUsername()).orElse(null);
        assertThat(deletedUser).isNull();
    }

}