package com.sree.ecommerce.service;

import com.sree.ecommerce.models.Product;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import static com.sree.ecommerce.models.EmailTemplates.ORDER_CONFIRMATION_TEMPLATE;
import static com.sree.ecommerce.models.EmailTemplates.PAYMENT_CONFIRMATION_TEMPLATE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendPaymentSuccessEmail(
            String destinationEmail,
            String customerName,
            BigDecimal amount,
            String orderReference
    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper =
                new MimeMessageHelper(mimeMessage, MULTIPART_MODE_MIXED_RELATED, UTF_8.name());
        messageHelper.setFrom("sgaddojuberlin@gmail.com");
        final String templateName = PAYMENT_CONFIRMATION_TEMPLATE.getTemplate();
        var variables = new HashMap<String, Object>();
        variables.put("customerName", customerName);
        variables.put("amount", amount);
        variables.put("orderReference", orderReference);

        Context context = new Context();
        context.setVariables(variables);
        messageHelper.setSubject(PAYMENT_CONFIRMATION_TEMPLATE.getSubject());

        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);
            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info("Payment Success Mail successfully sent to : {} with template : {}", destinationEmail, templateName);
        } catch (MessagingException ex) {
            log.warn("Cannot send payment success email to {}", destinationEmail);
        }
    }

    @Async
    public void sendOrderConfirmationEmail(
            String destinationEmail,
            String customerName,
            BigDecimal amount,
            String orderReference,
            List<Product> products
    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper =
                new MimeMessageHelper(mimeMessage, MULTIPART_MODE_MIXED_RELATED, UTF_8.name());
        messageHelper.setFrom("sgaddojuberlin@gmail.com");
        final String templateName = ORDER_CONFIRMATION_TEMPLATE.getTemplate();
        var variables = new HashMap<String, Object>();
        variables.put("customerName", customerName);
        variables.put("totalAmount", amount);
        variables.put("orderReference", orderReference);
        variables.put("products", products);

        Context context = new Context();
        context.setVariables(variables);
        messageHelper.setSubject(ORDER_CONFIRMATION_TEMPLATE.getSubject());

        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);
            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info("Order Confirmation Mail successfully sent to : {} with template : {}", destinationEmail, templateName);
        } catch (MessagingException ex) {
            log.warn("Cannot send order confirmation email to {}", destinationEmail);
        }
    }

}
