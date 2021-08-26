package org.thebreak.roombooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class MailSenderService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Async
    public void sendSimpleMessage( String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("breaknotification@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    @Async
    public void sendBookingConfirmEmailNotification(String to, String username, String roomTitle, LocalDateTime startDateTime, long totalHours, long totalPrice) throws MessagingException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a");
        System.out.println("start sending booking confirm email notification...");
        String formatedTime = startDateTime.format(dateTimeFormatter);
        String html = "<p>Hi, <span style='font-weight: bold'>" + username + "</span></p>\n" +
                "    <p>Thank you for booking at TheBreak!</p>\n" +
                "    <p>Your booking for: </p>\n" +
                "    <div style='background-color: lightskyblue; border-left: 4px solid black; padding: 8px 16px'><p>Room: <span>" + roomTitle + "</span></p>\n" +
                "    <p>Total of <span style='font-weight: bold'> " + totalHours + " hours</span> starting at <span style='font-weight: bold'>"+ formatedTime +"</span> with total amount of <span style='color: darkred'>$"+totalPrice+"</span></p></div>\n" +
                "    <p>is successful, please process the payment within 30 minutes. Unpaid bookings will be cancelled automatically after 30 minutes.</p>" +
                "</br>" +
                "<p>TheBreak Room booking team</p>";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("breaknotification@gmail.com");
        helper.setTo(to);
        helper.setSubject("Room booking success, please proceed to payment.");
        helper.setText(html, true);

        javaMailSender.send(message);

    }
}
