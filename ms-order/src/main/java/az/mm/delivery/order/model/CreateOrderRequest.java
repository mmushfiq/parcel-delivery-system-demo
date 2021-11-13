package az.mm.delivery.order.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateOrderRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String destination;

    private String note;

}
