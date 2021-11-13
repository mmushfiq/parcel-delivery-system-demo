package az.mm.delivery.notification.messaging;

import az.mm.delivery.notification.messaging.event.EmailNotificationEvent;
import az.mm.delivery.notification.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {

    private final MailService mailService;

    @Bean
    public Consumer<EmailNotificationEvent> receiveEmailNotificationEvent() {
        return event -> {
            log.info("Receive EmailNotificationEvent from ms-order: {}", event);
            mailService.sendParcelOrderInfo(event);
        };
    }

}
