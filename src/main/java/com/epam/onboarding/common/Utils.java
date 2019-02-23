package com.epam.onboarding.common;

import java.util.UUID;

public class Utils {

    private Utils() {
        throw new UnsupportedOperationException("Utils Class");
    }

    public static long randomLong() {
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }
}
