package fpt.swp.workspace.service;

import fpt.swp.workspace.models.Customer;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
public class SendEmailService {
    @Autowired
    private  JavaMailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;


    public void sendSimpleMessage(String toEmail){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("TEST");
        message.setText("Email test chức năng");
        emailSender.send(message);
    }

    public void sendHtmlMessage(String toEmail, String subject, String html){
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try{
            helper.setTo(toEmail);
            helper.setSubject(subject);
            try{
                InputStream inputStream = Objects.requireNonNull(SendEmailService.class.getResourceAsStream(html));
                helper.setText(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8), true);
            }catch (IOException e){
                System.out.println("IOException: " + e.getMessage());
            }
            emailSender.send(message);
        }catch (MessagingException e){
            e.printStackTrace();
        }
    }

    public void sendThymleafMessage(String toEmail, String subject, String templateName, Model model)  {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_RELATED, StandardCharsets.UTF_8.name());
            helper.setTo(toEmail);
            helper.setSubject(subject);

            // Directly render the Thymeleaf template within this method
            Context context = new Context();
            context.setVariables(model.asMap());

            String htmlContent = templateEngine.process(templateName, context);

            helper.setText(htmlContent, true);  // Set the HTML content to the message
            emailSender.send(message);  // Send the email
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }



}
