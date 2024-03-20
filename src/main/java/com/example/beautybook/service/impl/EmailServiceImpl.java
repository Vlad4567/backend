package com.example.beautybook.service.impl;

import com.example.beautybook.dto.DataForMailDto;
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
    public void sendMail(DataForMailDto dto) {
        String[] body = generateTextEmail(dto);
        emailSenderUtil.sendEmail(dto.getEmail(), body[0], body[1]);
    }

    private String[] generateTextEmail(DataForMailDto dto) {
        String greeting = "Hello, "
                + dto.getUsername()
                + System.lineSeparator()
                + System.lineSeparator();
        String message = "";
        String subject = "";
        switch (dto.getEmailType()) {
            case "verification":
                subject = "Verification email";
                message = "To verify your email, please follow the link below: "
                        + System.lineSeparator() + System.lineSeparator()
                        + host + contextPath + "/auth/verificationMail/"
                        + jwtUtil.generateToken(dto.getEmail(), JwtUtil.Secret.MAIL);
                break;
            case "passwordReset":
                subject = "Password Reset";
                message = "You are receiving this email because you "
                        + "have requested a password reset for your account"
                        + System.lineSeparator() + System.lineSeparator()
                        + "Your new temporary password: " + dto.getNewData();
                break;
            case "updateEmail":
                subject = "Update email";
                message = "To change the current Email to "
                        + dto.getNewData() + "follow this link: "
                        + System.lineSeparator() + System.lineSeparator()
                        + host + contextPath + "/user/updateEmail/"
                        + jwtUtil.generateToken(
                                dto.getEmail() + ":" + dto.getNewData(),
                                JwtUtil.Secret.MAIL);
                break;
            case "verificationNewEmail":
                subject = "Verification new email";
                message = "To verify your email, please follow the link below: "
                        + System.lineSeparator() + System.lineSeparator()
                        + host + contextPath + "/user/verificationNewMail/"
                        + jwtUtil.generateToken(dto.getNewData(), JwtUtil.Secret.MAIL);
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
