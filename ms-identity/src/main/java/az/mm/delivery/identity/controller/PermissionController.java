package az.mm.delivery.identity.controller;

import az.mm.delivery.common.error.exception.InvalidInputException;
import az.mm.delivery.identity.dto.PermissionDto;
import az.mm.delivery.identity.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    public List<PermissionDto> getAllPermissions() {
        return permissionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionDto> getPermission(@PathVariable Long id) {
        PermissionDto permissionDto = permissionService.findOne(id);
        return ResponseEntity.ok(permissionDto);
    }

    @PostMapping
    public ResponseEntity<PermissionDto> createPermission(@Valid @RequestBody PermissionDto permissionDto) {
        if (permissionDto.getId() != null)
            throw InvalidInputException.of("A new permission cannot already have an ID");

        PermissionDto result = permissionService.save(permissionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping
    public ResponseEntity<PermissionDto> updatePermission(@Valid @RequestBody PermissionDto permissionDto) {
        if (permissionDto.getId() == null)
            throw InvalidInputException.of("Permission ID cannot be null");

        PermissionDto result = permissionService.save(permissionDto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
