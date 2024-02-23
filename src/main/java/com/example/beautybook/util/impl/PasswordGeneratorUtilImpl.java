package com.example.beautybook.util.impl;

import com.example.beautybook.util.PasswordGeneratorUtil;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class PasswordGeneratorUtilImpl implements PasswordGeneratorUtil {
    private static final String LOWERCASE_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "@#$%^&+=!";
    private static final int LENGTH_PASSWORD = 12;
    private  static final Random RANDOM = new Random();

    @Override
    public String generateRandomPassword() {
        String allSymbol = LOWERCASE_CHARACTERS
                + UPPERCASE_CHARACTERS
                + DIGITS
                + SPECIAL_CHARACTERS;
        StringBuilder randomPassword = new StringBuilder();
        for (int i = 0; i < LENGTH_PASSWORD; i++) {
            int random = RANDOM.nextInt(allSymbol.length());
            randomPassword.append(allSymbol.indexOf(random));
        }
        return insertRandomSpecialCharacter(randomPassword.toString());
    }

    private String insertRandomSpecialCharacter(String password) {
        String regex = ".*[" + Pattern.quote(SPECIAL_CHARACTERS) + "].*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            int randomIndex = RANDOM.nextInt(password.length());
            int randomSymbol = RANDOM.nextInt(SPECIAL_CHARACTERS.length());
            return password.substring(0, randomIndex)
                    + SPECIAL_CHARACTERS.indexOf(randomSymbol)
                    + password.substring(randomIndex + 1);
        }
        return password;
    }
}
