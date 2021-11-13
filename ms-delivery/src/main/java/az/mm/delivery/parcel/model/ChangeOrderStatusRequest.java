package az.mm.delivery.parcel.model;

import az.mm.delivery.parcel.enums.OrderStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChangeOrderStatusRequest {

    @NotNull
    private Long orderId;

    @NotNull
    private OrderStatus newOrderStatus;

}
