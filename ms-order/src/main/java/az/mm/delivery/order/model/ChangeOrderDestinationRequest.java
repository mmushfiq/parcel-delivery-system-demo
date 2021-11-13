package az.mm.delivery.order.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ChangeOrderDestinationRequest {

    @NotNull
    private Long orderId;

    @NotBlank
    private String newDestination;

}
