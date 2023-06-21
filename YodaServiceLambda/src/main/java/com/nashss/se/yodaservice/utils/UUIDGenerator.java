package com.nashss.se.yodaservice.utils;

import java.util.UUID;

public class UUIDGenerator {

    private static final int ID_LENGTH = 10;

    public static String generateUniqueId() {
        UUID uuid = UUID.randomUUID();
        long mostSignificantBits = uuid.getMostSignificantBits();
        long leastSignificantBits = uuid.getLeastSignificantBits();
        long combinedBits = mostSignificantBits ^ leastSignificantBits;
        String id = Long.toString(combinedBits, 36).toUpperCase();
        return id.substring(Math.max(id.length() - ID_LENGTH, 0));
    }

    public static String randomTranscribeJobName
}
