package com.example.beautybook.message;

import java.util.HashMap;
import java.util.Map;

public class MessageProvider {
    private static final Map<String, String> messages = new HashMap<>();
    private static final String START_TEXT = "Hello, %s!"
            + System.lineSeparator() + System.lineSeparator();
    private static final String FINISH_TEXT =
            System.lineSeparator() + System.lineSeparator() + "Best regards, team Sparkle";

    static {
        messages.put("login.code", "Login Code:" + System.lineSeparator()
                + System.lineSeparator()
                + "*%s*"
                + System.lineSeparator() + System.lineSeparator()
                + "Enter this code on the [Sparkly](https://google.com) website"
                + System.lineSeparator() + System.lineSeparator()
                + "The code will expire in: 60 seconds"
        );
        messages.put("verification.code", "Verification Code:" + System.lineSeparator()
                + System.lineSeparator()
                + "*%s*"
                + System.lineSeparator() + System.lineSeparator()
                + "Enter this code on the [Sparkle](https://google.com) website"
                + System.lineSeparator() + System.lineSeparator()
                + "The code will expire in: 60 seconds");
        messages.put("user.registration", "User registration was successful"
                + System.lineSeparator() + System.lineSeparator()
                + "Account Telegram" + System.lineSeparator()
                + "username: %s" + System.lineSeparator()
                + "chatId: %s" + System.lineSeparator()
                + System.lineSeparator()
                + "Now you can proceed to the [Sparkle](https://google.com) "
                + "website and log in using Telegram.");
        messages.put("contact.form", "Dear User" + System.lineSeparator()
                + "Thank you for reaching out to us through our contact form."
                + System.lineSeparator() + System.lineSeparator()
                + "Your message:" + System.lineSeparator()
                + "%s" + System.lineSeparator()
                + System.lineSeparator()
                + "Response to your inquiry:" + System.lineSeparator()
                + "%s" + System.lineSeparator()
                + FINISH_TEXT);
        messages.put("confirm.registration", "To confirm the registration of a new user, "
                + "click \"confirm\"");
        messages.put("start", START_TEXT + "I'm the Sparkly bot." + System.lineSeparator()
                + "With me, you can quickly and easily register on Sparkly,"
                + " log in to your account, verify your Telegram account, "
                + "or contact customer support for any questions!" + System.lineSeparator()
                + System.lineSeparator()
                + "What would you like to do?");
        messages.put("delete.telegram", "Are you sure you want to unlink "
                + "this Telegram account from user Sparkle: "
                + System.lineSeparator() + "username: %s");
        messages.put("contact.form.telegram", "#ContactFormaId-%s;"
                + System.lineSeparator() + System.lineSeparator()
                + "Email: %s" + System.lineSeparator()
                + System.lineSeparator()
                + "Message:" + System.lineSeparator()
                + "%s");
        messages.put("password", "This is your temporary password." + System.lineSeparator()
                + "Please change it to your permanent password on the Sparkle website.");
        messages.put("verification.email", START_TEXT
                + "To verify your email, please follow the link below: "
                + System.lineSeparator() + System.lineSeparator()
                + "%s"
                + FINISH_TEXT);
        messages.put("password.reset", START_TEXT
                + "You are receiving this email because you "
                + "have requested a password reset for your account"
                + System.lineSeparator() + System.lineSeparator()
                + "Your new temporary password: %s" + FINISH_TEXT);
        messages.put("new.review", "NEW Review #%s!"
                + System.lineSeparator() + System.lineSeparator()
                + "Username: %s" + System.lineSeparator() + "%s"
                + System.lineSeparator() + System.lineSeparator()
                + "Comment: " + System.lineSeparator() + "*%s*");
    }

    public static String getMessage(String key, Object... args) {
        String message = messages.getOrDefault(key, "Сообщение не найдено для ключа: " + key);
        if (args.length > 0) {
            message = String.format(message, args);
        }
        return message;
    }
}
