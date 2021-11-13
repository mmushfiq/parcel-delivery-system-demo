package az.mm.delivery.notification.service;

import az.mm.delivery.notification.messaging.event.EmailNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class MailService {

    @Value("${application.email.from}")
    private String mailSender;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    public MailService(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendParcelOrderInfo(EmailNotificationEvent event) {
        Context context = new Context();
        context.setVariable("event", event);
        context.setVariable("imageResourceName", "https://images-platform.99static.com//DaTIx2162nmq5AJpEwQWugAXp00=/303x295:1702x1694/fit-in/500x500/99designs-contests-attachments/103/103961/attachment_103961129");
        String content = templateEngine.process("parcelOrderInfo", context);
        sendEmail(event.getEmail(), "Parcel Order Info", content, false, true);
    }

    private void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setFrom(mailSender);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (Exception e) {
            log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
        }
    }

}
