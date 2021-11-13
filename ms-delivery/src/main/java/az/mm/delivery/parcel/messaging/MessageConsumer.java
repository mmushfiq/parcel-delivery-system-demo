package az.mm.delivery.parcel.messaging;

import az.mm.delivery.parcel.messaging.event.ParcelOrderEvent;
import az.mm.delivery.parcel.service.ParcelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {

    private final ParcelService parcelService;

    @Bean
    public Consumer<ParcelOrderEvent> receiveParcelOrderEvent() {
        return event -> {
            log.info("Received ParcelOrderEvent from ms-order: {}", event);
            parcelService.processParcelOrderEvent(event);
        };
    }

}
