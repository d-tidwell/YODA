package com.nashss.se.yodaservice.utils;


public class Sanitizer {

    public static String sanitizeField(String field) {
        if (field != null) {
            // Remove illegal characters
            return field.replaceAll("[|/\\\\#<>?\";:{}^\\[\\]`~]", "");
        }
        throw new IllegalArgumentException("Field names must not have illegal characters!");
    }
}