package az.mm.delivery.parcel.messaging.event;

import az.mm.delivery.parcel.enums.OrderStatus;
import lombok.Data;

@Data
public class ParcelOrderEvent {

    private String orderNumber;

    private String destination;

    private OrderStatus status;

    private String courier;

    private String coordination;

}
