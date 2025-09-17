package com.bubblecraft.bubblereset;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ColorUtil {
    private static final Pattern HEX_PATTERN = Pattern.compile("<#([A-Fa-f0-9]{6})>");

    private ColorUtil() {}

    public static String colorize(String input) {
        if (input == null) return "";
        // Support hex pattern <#RRGGBB>
        Matcher matcher = HEX_PATTERN.matcher(input);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String hex = matcher.group(1);
            matcher.appendReplacement(buffer, "§x§" + hex.charAt(0) + "§" + hex.charAt(1) +
                    "§" + hex.charAt(2) + "§" + hex.charAt(3) +
                    "§" + hex.charAt(4) + "§" + hex.charAt(5));
        }
        matcher.appendTail(buffer);
        // Translate legacy color codes '&'
        return buffer.toString().replace('&', '§');
    }
}
