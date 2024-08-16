package com.ecommercial.notification.service;

import com.ecommercial.notification.kafka.order.Product;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ecommercial.notification.entity.EmailTemplate.*;
import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@RequiredArgsConstructor
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sentPaymentSuccessEmail(
            String destinationEmail,
            String customerName,
            BigDecimal amount,
            String orderReference
    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper =
                new MimeMessageHelper(mimeMessage,MimeMessageHelper.MULTIPART_MODE_RELATED, UTF_8.name());

        messageHelper.setFrom("nghialmao@gmail.comm");
        final String templateName = PAYMENT_CONFIRMATION.getTemplate();


        Map<String, Object> variables = new HashMap<>();

        variables.put("customerName", customerName);
        variables.put("amount", amount);
        variables.put("orderReference",orderReference);

        Context context = new Context();

        context.setVariables(variables);
        messageHelper.setSubject(PAYMENT_CONFIRMATION.getSubject());

        try
        {
            String htmltemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmltemplate,true);

            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);

            log.info(String.format("INFO - Email successfully sent to %s with template %s ", destinationEmail, templateName));
        }
        catch (MessagingException exception)
        {
            log.warn("WARNING - Cannot send Email to {} ", destinationEmail);
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
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, UTF_8.name());
        messageHelper.setFrom("contact@aliboucoding.com");

        final String templateName = ORDER_CONFIRMATION.getTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("totalAmount", amount);
        variables.put("orderReference", orderReference);
        variables.put("products", products);

        Context context = new Context();
        context.setVariables(variables);
        messageHelper.setSubject(ORDER_CONFIRMATION.getSubject());

        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);

            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info(String.format("INFO - Email successfully sent to %s with template %s ", destinationEmail, templateName));
        } catch (MessagingException e) {
            log.warn("WARNING - Cannot send Email to {} ", destinationEmail);
        }

    }
}
