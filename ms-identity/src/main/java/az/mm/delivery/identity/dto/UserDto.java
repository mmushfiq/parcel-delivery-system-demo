package az.mm.delivery.identity.dto;

import az.mm.delivery.common.enums.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
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
public class UserDto implements Serializable {

    private Long id;

    @Email
    private String username;

    @JsonIgnore
    @NotNull
    private String password;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private UserType type;

    @NotNull
    private String phone;

    private Boolean enabled;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder.Default
    private Set<RoleSmallDto> roles = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return id.equals(userDto.id) && username.equals(userDto.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

}
