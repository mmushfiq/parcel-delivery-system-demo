package az.mm.delivery.parcel.messaging.event;

import az.mm.delivery.parcel.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusChangeEvent {

    private String orderNumber;

    private OrderStatus orderStatus;

}
