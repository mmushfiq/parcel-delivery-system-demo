package az.mm.delivery.order.messaging.event;

import az.mm.delivery.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParcelOrderEvent {

    private String orderNumber;

    private String destination;

    private OrderStatus status;

    private String courier;

    private String coordination;

}
