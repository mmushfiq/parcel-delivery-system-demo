package az.mm.delivery.identity.mapper;

import az.mm.delivery.identity.domain.Role;
import az.mm.delivery.identity.dto.RoleDto;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RoleMapperTest {

    private final RoleMapper roleMapper = RoleMapper.INSTANCE;
    private static final LocalDateTime CREATION_DATETIME = LocalDateTime.now();

    private static final Role ROLE = Role.builder()
            .id(654L)
            .name("user")
            .description("can only view")
            .createdAt(Timestamp.valueOf(CREATION_DATETIME))
            .permissions(Collections.emptySet())
            .build();

    private static final RoleDto ROLE_DTO = RoleDto.builder()
            .id(654L)
            .name("user")
            .description("can only view")
            .permissions(Collections.emptySet())
            .createdAt(CREATION_DATETIME)
            .build();

    @Test
    void toRoleDtoTest() {
        assertEquals(ROLE_DTO, roleMapper.toDto(ROLE));
    }

    @Test
    void fromId() {
        Long id = 654L;
        assertEquals(id, roleMapper.fromId(id).getId());
        assertNull(roleMapper.fromId(null));
    }

}