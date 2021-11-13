package az.mm.delivery.order.messaging;

import az.mm.delivery.order.messaging.event.EmailNotificationEvent;
import az.mm.delivery.order.messaging.event.ParcelOrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageProducer {

    private final StreamBridge streamBridge;

    public void sendParcelOrderEvent(ParcelOrderEvent event) {
        log.info("Send ParcelOrderEvent to ms-delivery: {}", event);
        streamBridge.send(OutputChannel.PARCEL_ORDER, event);
    }

    public void sendEmailNotificationEvent(EmailNotificationEvent event) {
        log.info("Send EmailNotificationEvent to ms-notification: {}", event);
        streamBridge.send(OutputChannel.EMAIL_NOTIFICATION, event);
    }

}
