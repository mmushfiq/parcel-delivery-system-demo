package az.mm.delivery.identity.mapper;

import az.mm.delivery.identity.domain.User;
import az.mm.delivery.identity.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper extends EntityMapper<UserDto, User> {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    default User fromId(Long id) {
        if (id == null) return null;
        User user = new User();
        user.setId(id);
        return user;
    }

}
