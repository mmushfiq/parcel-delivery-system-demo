package az.mm.delivery.order.model;

import az.mm.delivery.order.enums.OrderStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChangeOrderStatusRequest {

    @NotNull
    private Long orderId;

    @NotNull
    private OrderStatus newOrderStatus;

}
