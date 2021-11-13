package az.mm.delivery.identity.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    @ApiModelProperty(value = "Username should be email", example = "name.surname@email.com")
    @Email
    private String username;

    @NotNull
    @Size(min = 8, max = 20)
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    @Size(min = 10, max = 14)
    private String phone;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SignupRequest{");
        sb.append("username='").append(username).append('\'');
        sb.append(", password='").append("******").append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
