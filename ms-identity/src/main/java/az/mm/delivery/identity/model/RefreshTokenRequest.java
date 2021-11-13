package az.mm.delivery.identity.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RefreshTokenRequest {

    @NotEmpty
    private String refreshToken;

}
