package com.Server.utils;

import com.Server.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.security.SecureRandom;

public class Utils {
    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();
    public static String generateRandomConfirmationCode(int length) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }

    public static Request parseStringToJSonData(String formData) {
        try {
            return new ObjectMapper().readValue(formData, Request.class);
        } catch (Exception e) {
            return null;
        }
    }
}