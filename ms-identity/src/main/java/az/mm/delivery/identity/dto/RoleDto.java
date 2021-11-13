package az.mm.delivery.identity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    private LocalDateTime createdAt;

    @Builder.Default
    private Set<PermissionSmallDto> permissions = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleDto roleDto = (RoleDto) o;
        return id.equals(roleDto.id) && name.equals(roleDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
