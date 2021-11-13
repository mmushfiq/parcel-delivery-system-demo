package az.mm.delivery.identity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenPair implements Serializable {

    private String accessToken;

    private String refreshToken;

}
