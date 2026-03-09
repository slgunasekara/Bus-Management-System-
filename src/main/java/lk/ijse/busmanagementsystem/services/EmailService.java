package lk.ijse.busmanagementsystem.services;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.Random;

public class EmailService {


    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL_FROM = "praveengunasekara7@gmail.com";
    private static final String EMAIL_PASSWORD = "czwv trod ller weih";

    public static String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public static boolean sendOTPEmail(String toEmail, String otpCode, String userName) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.ssl.trust", SMTP_HOST);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_FROM, EMAIL_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_FROM, "Gunasekara Travels"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Password Reset OTP - Gunasekara Travels");

            String emailContent = buildEmailContent(otpCode, userName);
            message.setContent(emailContent, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("✓ OTP email sent successfully to: " + toEmail);
            return true;

        } catch (Exception e) {
            System.err.println("✗ Failed to send email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //Builds HTML email content with OTP
    private static String buildEmailContent(String otpCode, String userName) {
        return "<!DOCTYPE html>" +
                "<html><head><style>" +
                "body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }" +
                ".container { max-width: 600px; margin: 0 auto; background: white; border-radius: 10px; " +
                "box-shadow: 0 2px 10px rgba(0,0,0,0.1); overflow: hidden; }" +
                ".header { background: linear-gradient(135deg, #3498db 0%, #1a5580 100%); " +
                "color: white; padding: 30px; text-align: center; }" +
                ".header h1 { margin: 0; font-size: 28px; }" +
                ".content { padding: 40px 30px; }" +
                ".otp-box { background: #f8f9fa; border: 2px dashed #3498db; border-radius: 8px; " +
                "padding: 20px; text-align: center; margin: 30px 0; }" +
                ".otp-code { font-size: 36px; font-weight: bold; color: #3498db; " +
                "letter-spacing: 8px; margin: 10px 0; }" +
                ".warning { background: #fff3cd; border-left: 4px solid #ffc107; " +
                "padding: 15px; margin: 20px 0; }" +
                ".footer { background: #f8f9fa; padding: 20px; text-align: center; " +
                "color: #6c757d; font-size: 12px; }" +
                "</style></head><body>" +
                "<div class='container'>" +
                "<div class='header'><h1>🔐 Password Reset Request</h1></div>" +
                "<div class='content'>" +
                "<p>Dear <strong>" + userName + "</strong>,</p>" +
                "<p>We received a request to reset your password for your Gunasekara Travels account.</p>" +
                "<p>Your One-Time Password (OTP) is:</p>" +
                "<div class='otp-box'><div class='otp-code'>" + otpCode + "</div>" +
                "<p style='margin: 5px 0; color: #6c757d;'>Valid for 10 minutes</p></div>" +
                "<div class='warning'><strong>⚠️ Security Notice:</strong><br>" +
                "• Do not share this OTP with anyone<br>" +
                "• This OTP will expire in 10 minutes<br>" +
                "• If you didn't request this, please ignore this email</div>" +
                "<p>Thank you,<br><strong>Gunasekara Travels Team</strong></p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>© 2025 Gunasekara Travels - Bus Management System<br>" +
                "This is an automated message, please do not reply.</p>" +
                "</div></div></body></html>";
    }

    public static void main(String[] args) {
        String testOTP = generateOTP();
        System.out.println("Generated OTP: " + testOTP);
        boolean sent = sendOTPEmail("test@example.com", testOTP, "Test User");
        System.out.println("Email sent: " + sent);
    }
}