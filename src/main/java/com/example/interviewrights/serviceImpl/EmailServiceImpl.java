package com.example.interviewrights.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.interviewrights.request.InterviewRequest;
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

	@Override
	public void sendInterviewMail(InterviewRequest req) {

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom(fromEmail);
			helper.setTo(req.getEmail());
			helper.setSubject("Interview Scheduled");

			String emailContent = """
					    <p>Dear Candidate,</p>

					    <p>Your interview has been <b>scheduled</b>. Please find the details below:</p>

					    <table style="border-collapse: collapse; margin-top: 10px;">
					        <tr>
					            <td style="padding: 8px;"><b>Date:</b></td>
					            <td style="padding: 8px;">%s</td>
					        </tr>
					        <tr>
					            <td style="padding: 8px;"><b>Time:</b></td>
					            <td style="padding: 8px;">%s</td>
					        </tr>
					        <tr>
					            <td style="padding: 8px;"><b>Meeting Link:</b></td>
					            <td style="padding: 8px;">
					                <a href="%s" target="_blank">Join Interview</a>
					            </td>
					        </tr>
					    </table>

					    <br>

					    <p>Please make sure to join on time.</p>

					    <br>

					    <p>Regards,<br><b>HR Team</b></p>
					""".formatted(req.getDate(), req.getTime(), req.getLink());

			helper.setText(emailContent, true);
			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void sendInterviewMail(String email, String link) {

	    SimpleMailMessage message = new SimpleMailMessage();

	    message.setTo(email);
	    message.setSubject("Interview Invitation");

	    message.setText(
	        "Dear Candidate,\n\n" +
	        "Your interview is scheduled.\n\n" +
	        "Click below to join (secure link):\n" +
	        link + "\n\n" +
	        "Note: Link will expire automatically.\n\n" +
	        "Best Regards"
	    );

	    mailSender.send(message);
	}
	
	public void sendInterviewMailForInterviewer(String email, String link) {

	    SimpleMailMessage message = new SimpleMailMessage();

	    message.setTo(email);
	    message.setSubject("Interview Invitation");

	    message.setText(
	        "Dear Interviewer,\n\n" +
	        "Your interview is scheduled.\n\n" +
	        "Click below to join (secure link):\n" +
	        link + "\n\n" +
	        "Note: Link will expire automatically.\n\n" +
	        "Best Regards"
	    );

	    mailSender.send(message);
	}
	
}