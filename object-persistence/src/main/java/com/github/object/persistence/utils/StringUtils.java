package com.github.object.persistence.utils;

public final class StringUtils {

    private StringUtils() {}

    public static boolean isBlank(String string) {
        return string == null || string.isBlank();
    }

    public static String separateWithSpace(String... string) {
        return String.join(" ", string);
    }
}
