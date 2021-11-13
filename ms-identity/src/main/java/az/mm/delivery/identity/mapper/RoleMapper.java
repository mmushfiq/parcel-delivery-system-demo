package az.mm.delivery.identity.mapper;

import az.mm.delivery.identity.domain.Role;
import az.mm.delivery.identity.dto.RoleDto;
import az.mm.delivery.identity.dto.RoleSmallDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper extends EntityMapper<RoleDto, Role> {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    @Mapping(target = "users", ignore = true)
    Role toEntity(RoleDto roleDto);

    @Mapping(target = "users", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    Role toEntity(RoleSmallDto roleSmallDto);

    RoleSmallDto toSmallDto(Role role);

    default Role fromId(Long id) {
        if (id == null) return null;
        Role role = new Role();
        role.setId(id);
        return role;
    }

}
