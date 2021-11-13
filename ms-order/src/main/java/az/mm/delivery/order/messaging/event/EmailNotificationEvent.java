package az.mm.delivery.order.messaging.event;

import az.mm.delivery.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
