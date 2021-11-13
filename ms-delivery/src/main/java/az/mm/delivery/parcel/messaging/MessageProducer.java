package az.mm.delivery.parcel.messaging;

import az.mm.delivery.parcel.messaging.event.OrderStatusChangeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageProducer {

    private final StreamBridge streamBridge;

    public void sendOrderStatusChangeEvent(OrderStatusChangeEvent event) {
        log.info("Send OrderStatusChangeEvent to ms-order: {}", event);
        streamBridge.send(OutputChannel.ORDER_STATUS_CHANGE, event);
    }

}
