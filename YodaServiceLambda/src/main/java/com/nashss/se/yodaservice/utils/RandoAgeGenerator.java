package com.nashss.se.yodaservice.utils;

import java.util.Random;

public class RandoAgeGenerator {
    public static String generateRandomAge() {
        Random random = new Random();
        int minAge = 18;
        int maxAge = 78;
        int ageRange = maxAge - minAge + 1;

        return String.valueOf(random.nextInt(ageRange) + minAge);
    }
}
