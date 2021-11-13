package az.mm.delivery.identity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SigninRequest {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @Override
    public String toString() {
        return "SigninRequest{" +
                "username='" + username + '\'' +
                ", password='******'" +
                '}';
    }
}
