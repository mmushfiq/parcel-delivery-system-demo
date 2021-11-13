package az.mm.delivery.order.messaging;

import az.mm.delivery.order.messaging.event.OrderStatusChangeEvent;
import az.mm.delivery.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {

    private final OrderService orderService;

    @Bean
    public Consumer<OrderStatusChangeEvent> receiveOrderStatusChangeEvent() {
        return event -> {
            log.info("Receive OrderStatusChangeEvent from ms-delivery: {}", event);
            orderService.changeStatus(event);
        };
    }

}
