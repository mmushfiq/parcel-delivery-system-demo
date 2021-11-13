package az.mm.delivery.identity.controller;

import az.mm.delivery.common.error.exception.InvalidInputException;
import az.mm.delivery.identity.dto.RoleDto;
import az.mm.delivery.identity.service.RoleService;
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
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public List<RoleDto> getAllRoles() {
        return roleService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getRole(@PathVariable Long id) {
        RoleDto roleDto = roleService.findOne(id);
        return ResponseEntity.ok(roleDto);
    }

    @PostMapping
    public ResponseEntity<RoleDto> createRole(@Valid @RequestBody RoleDto roleDto) {
        if (roleDto.getId() != null)
            throw InvalidInputException.of("A new role cannot already have an ID");

        RoleDto result = roleService.save(roleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping
    public ResponseEntity<RoleDto> updateRole(@Valid @RequestBody RoleDto roleDto) {
        if (roleDto.getId() == null)
            throw InvalidInputException.of("Role ID cannot be null");

        RoleDto result = roleService.save(roleDto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
