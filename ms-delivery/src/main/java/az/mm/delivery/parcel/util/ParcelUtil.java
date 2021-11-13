package az.mm.delivery.parcel.util;

import az.mm.delivery.parcel.dto.ParcelDto;
import az.mm.delivery.parcel.messaging.event.OrderStatusChangeEvent;
import az.mm.delivery.parcel.messaging.event.ParcelOrderEvent;

public final class ParcelUtil {

    private ParcelUtil() {
    }

    public static OrderStatusChangeEvent createOrderStatusChangeEvent(ParcelDto dto) {
        return OrderStatusChangeEvent.builder()
                .orderNumber(dto.getOrderNumber())
                .orderStatus(dto.getStatus())
                .build();
    }

    public static ParcelDto createParcelDto(ParcelOrderEvent event) {
        return ParcelDto.builder()
                .orderNumber(event.getOrderNumber())
                .destination(event.getDestination())
                .status(event.getStatus())
                .courier(event.getCourier())
                .coordination(event.getCoordination())
                .build();
    }

}
