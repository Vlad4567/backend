package com.example.demo;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Password {
    private static final String LOWERCASE_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "@#$%^&+=!";
    private static final int LENGTH_PASSWORD = 12;
    private static final Random RANDOM = new Random();

    public static String generateRandomPassword() {
        String allSymbol = LOWERCASE_CHARACTERS
                + UPPERCASE_CHARACTERS
                + DIGITS
                + SPECIAL_CHARACTERS;
        StringBuilder randomPassword = new StringBuilder();
        for (int i = 0; i < LENGTH_PASSWORD; i++) {
            int random = RANDOM.nextInt(allSymbol.length() - 1);
            randomPassword.append(allSymbol.charAt(random));
        }
        return insertRandomSpecialCharacter(randomPassword.toString());
    }

    private static String insertRandomSpecialCharacter(String password) {
        String regex = ".*[" + Pattern.quote(SPECIAL_CHARACTERS) + "].*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            int randomIndex = RANDOM.nextInt(password.length());
            int randomSymbol = RANDOM.nextInt(SPECIAL_CHARACTERS.length());
            return password.substring(0, randomIndex)
                    + SPECIAL_CHARACTERS.charAt(randomSymbol)
                    + password.substring(randomIndex + 1);
        }
        return password;
    }
}
