package az.mm.delivery.identity.service;

import az.mm.delivery.common.error.exception.InvalidInputException;
import az.mm.delivery.identity.domain.Permission;
import az.mm.delivery.identity.dto.PermissionDto;
import az.mm.delivery.identity.error.validation.ValidationMessage;
import az.mm.delivery.identity.mapper.PermissionMapper;
import az.mm.delivery.identity.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public PermissionDto save(PermissionDto permissionDto) {
        Permission permission = permissionMapper.toEntity(permissionDto);
        permission = permissionRepository.save(permission);
        return permissionMapper.toDto(permission);
    }

    @Transactional(readOnly = true)
    public List<PermissionDto> findAll() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PermissionDto findOne(Long id) {
        return permissionRepository.findById(id)
                .map(permissionMapper::toDto)
                .orElseThrow(() -> InvalidInputException.of(ValidationMessage.PERMISSION_NOT_FOUND, List.of(id)));
    }

    public void delete(Long id) {
        permissionRepository.deleteById(id);
    }

}
