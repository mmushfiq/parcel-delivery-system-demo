package az.mm.delivery.identity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleSmallDto implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleSmallDto roleDto = (RoleSmallDto) o;
        return id.equals(roleDto.id) && name.equals(roleDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
