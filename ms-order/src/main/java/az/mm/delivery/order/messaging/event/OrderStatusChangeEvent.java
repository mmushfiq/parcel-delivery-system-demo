package az.mm.delivery.order.messaging.event;

import az.mm.delivery.order.enums.OrderStatus;
import lombok.Data;

@Data
public class OrderStatusChangeEvent {

    private String orderNumber;

    private OrderStatus orderStatus;

}
