package ru.devtrifanya.online_store.services;

import lombok.RequiredArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService {
    private final JavaMailSender mailSender;

    /**
     * Отправка email с информацией о заказе.
     */
    public void sendEmailWithOrderInfo(String orderInfo, String userEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("trifanya.dev@gmail.com");
        message.setTo(userEmail);
        message.setSubject("Your order in Trifanya's online store.");
        message.setText(orderInfo);

        mailSender.send(message);
    }
}
