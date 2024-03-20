package com.example.beautybook.util.impl;

import com.example.beautybook.util.PasswordGeneratorUtil;
import java.util.Random;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PasswordGeneratorUtilImpl implements PasswordGeneratorUtil {
    private static final String LOWERCASE_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "@#$%^&+=!";
    private static final int LENGTH_PASSWORD = 8;
    private static Random RANDOM;

    @Autowired
    public PasswordGeneratorUtilImpl(Random random) {
        RANDOM = random;
    }

    @Override
    public String generateRandomPassword() {
        String allSymbol = LOWERCASE_CHARACTERS
                + UPPERCASE_CHARACTERS
                + DIGITS
                + SPECIAL_CHARACTERS;
        StringBuilder randomPassword = new StringBuilder();
        for (int i = 0; i < LENGTH_PASSWORD; i++) {
            int random = RANDOM.nextInt(allSymbol.length());
            randomPassword.append(allSymbol.charAt(random));
        }
        return insertRandomSpecialCharacter(randomPassword);
    }

    private String insertRandomSpecialCharacter(StringBuilder password) {
        String[] characters = new String[]{
                SPECIAL_CHARACTERS,
                LOWERCASE_CHARACTERS,
                UPPERCASE_CHARACTERS,
                DIGITS
        };
        for (String chars : characters) {
            String regex = ".*[" + Pattern.quote(chars) + "].*";
            Pattern pattern = Pattern.compile(regex);
            if (!pattern.matcher(password).matches()) {
                int randomSymbol = RANDOM.nextInt(chars.length());
                password.append(chars.charAt(randomSymbol));
            }
        }
        return password.toString();
    }
}
