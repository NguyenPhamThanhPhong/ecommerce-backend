package ecommerce.api.service.smtp;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

@Service
public class SMTPService {
    private final JavaMailSender mailSender;
    private final String DEFAULT_HTML_MESSAGE = """
            <html>
            <body>
                <h1>Your OTP is: %s</h1>
                <p>Please use this OTP to complete your transaction.</p>
            </body>
            </html>
            """;

    public SMTPService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        // Enable HTML

        mailSender.send(message);
    }

    public void sendOTPEmail(String to, String otp) throws MessagingException {
        String body = String.format(DEFAULT_HTML_MESSAGE, otp);
        sendEmail(to, "Phong Shop: Your OTP Code is ...", body);
    }
}