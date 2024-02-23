package com.example.beautybook.service.impl;

import com.example.beautybook.model.User;
import com.example.beautybook.security.JwtUtil;
import com.example.beautybook.service.EmailService;
import com.example.beautybook.util.EmailSenderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    @Value("${path.host}")
    private String host;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    private final EmailSenderUtil emailSenderUtil;
    private final JwtUtil jwtUtil;

    @Override
    public void sendMail(User user, String emailType) {
        String[] body = generateTextEmail(user, emailType);
        emailSenderUtil.sendEmail(user.getEmail(), body[0], body[1]);
    }

    private String[] generateTextEmail(User user, String emailType) {
        String greeting = "Hello, "
                + user.getUserName()
                + System.lineSeparator()
                + System.lineSeparator();
        String message = "";
        String subject = "";
        switch (emailType) {
            case "verification":
                subject = "Verification email";
                message = "To verify your email, please follow the link below: "
                        + System.lineSeparator() + System.lineSeparator()
                        + host + contextPath + "/auth/verificationMail/"
                        + jwtUtil.generateToken(user.getEmail(), JwtUtil.Secret.MAIL);
                break;
            case "passwordReset":
                subject = "Password Reset";
                message = "You are receiving this email because you "
                        + "have requested a password reset for your account"
                        + System.lineSeparator() + System.lineSeparator()
                        + "Your new temporary password: " + user.getPassword();
                break;
            default:
                break;
        }

        String signature = System.lineSeparator()
                + System.lineSeparator()
                + "Best regards, team Sparkle";

        return new String[]{subject, greeting + message + signature};
    }
}
