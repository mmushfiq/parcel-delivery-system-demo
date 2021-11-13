package az.mm.delivery.notification.messaging.event;

import az.mm.delivery.notification.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EmailNotificationEvent {

    private String orderNumber;

    private String name;

    private String destination;

    private OrderStatus status;

    private Double weight;

    private BigDecimal amount;

    private String email;

    private String message;

}
