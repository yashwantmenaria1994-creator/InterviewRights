package com.example.interviewrights.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.interviewrights.service.EmailService;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendResetPasswordEmail(String toEmail, String resetLink) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Interview-Rights | Reset Your Password");

            String emailContent = """
                    <p>Hello,</p>
                    <p>You requested to reset your password.</p>
                    <p>Click the link below to reset your password:</p>
                    <p><a href="%s">Reset Password</a></p>
                    <br>
                    <p>This link will expire in <b>15 minutes</b>.</p>
                    <p>If you didn’t request this, please ignore this email.</p>
                    <br>
                    <p>Regards,<br><b>Interview-Rights Team</b></p>
                    """.formatted(resetLink);

            helper.setText(emailContent, true);
            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send email");
        }
    }
}
