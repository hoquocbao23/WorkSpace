package fpt.swp.workspace.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
public class SendEmailService {
    @Autowired
    private  JavaMailSender emailSender;

    public void sendSimpleMessage(String toEmail){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("TEST");
        message.setText("Email test chức năng");
        emailSender.send(message);
    }

    public void sendHtmlMessage(String toEmail){
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try{
            helper.setTo(toEmail);
            helper.setSubject("XÁC NHẬN ĐẶT PHÒNG.");
            try{
                InputStream inputStream = Objects.requireNonNull(SendEmailService.class.getResourceAsStream("/templates/email-format.html"));
                helper.setText(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8), true);
            }catch (IOException e){
                System.out.println("IOException: " + e.getMessage());
            }
            emailSender.send(message);
        }catch (MessagingException e){
            e.printStackTrace();
        }
    }
}
