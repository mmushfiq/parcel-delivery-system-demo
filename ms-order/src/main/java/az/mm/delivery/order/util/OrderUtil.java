package az.mm.delivery.order.util;

import az.mm.delivery.order.dto.OrderDto;
import az.mm.delivery.order.enums.NotificationMessage;
import az.mm.delivery.order.messaging.event.EmailNotificationEvent;
import az.mm.delivery.order.messaging.event.ParcelOrderEvent;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public final class OrderUtil {

    private static final double DELIVERY_PRICE_PER_WEIGHT = 5.0;
    private static final String MAIN_WAREHOUSE_LOCATION = "40.4403340:49.8075000";

    private OrderUtil() {
    }

    public static BigDecimal calculateTotalDeliveryAmount(final double weight) {
        return BigDecimal.valueOf(weight).multiply(BigDecimal.valueOf(DELIVERY_PRICE_PER_WEIGHT));
    }

    public static double calculateRandomWeight() {
        DecimalFormat df = new DecimalFormat("#.###");
        return Double.parseDouble(df.format(Math.random()));
    }

    public static ParcelOrderEvent createParcelOrderEvent(OrderDto dto) {
        return ParcelOrderEvent.builder()
                .orderNumber(dto.getOrderNumber())
                .destination(dto.getDestination())
                .status(dto.getStatus())
                .courier(dto.getCourier())
                .coordination(getWarehouseLocation())
                .build();
    }

    public static String getWarehouseLocation() {
        return MAIN_WAREHOUSE_LOCATION;  // default location
    }

    public static EmailNotificationEvent createEmailNotificationEvent(OrderDto dto, NotificationMessage notif) {
        return EmailNotificationEvent.builder()
                .orderNumber(dto.getOrderNumber())
                .name(dto.getName())
                .destination(dto.getDestination())
                .status(dto.getStatus())
                .weight(dto.getWeight())
                .amount(dto.getAmount())
                .email(dto.getCreatedBy())
                .message(notif.message())
                .build();
    }

}
