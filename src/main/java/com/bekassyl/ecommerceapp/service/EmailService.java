package com.bekassyl.ecommerceapp.service;

import com.bekassyl.ecommerceapp.model.AppUser;
import com.bekassyl.ecommerceapp.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOrderConfirmation(Order order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(order.getAppUser().getEmail());
        message.setSubject("Order confirmation");
        message.setText("Your order has been confirmed. Order ID " + order.getId());
        mailSender.send(message);
    }

    public void sendConfirmationCode(AppUser appUser) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(appUser.getEmail());
        message.setSubject("Confirm your email");
        message.setText("Please confirm your email by entering this code " + appUser.getConfirmationCode() + ".");
        mailSender.send(message);
    }
}
