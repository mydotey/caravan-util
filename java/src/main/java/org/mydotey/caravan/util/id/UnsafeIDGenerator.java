package org.mydotey.caravan.util.id;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;

import org.mydotey.java.StringExtension;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class UnsafeIDGenerator {

    private static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    private static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");
    private static final String TIME_FORMAT = "yyMMddHHmmssSSS";

    public static final int TIME_BASED_RANDOM_MIN_LENGTH = TIME_FORMAT.length() + 3;

    public UnsafeIDGenerator() {

    }

    public static String random(int length) {
        if (length <= 0)
            return StringExtension.EMPTY;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(CHARS[ThreadLocalRandom.current().nextInt(CHARS.length)]);
        }

        return sb.toString();
    }

    public static String timeBasedRandom(int length) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_FORMAT);
        simpleDateFormat.setTimeZone(UTC_TIME_ZONE);
        if (length < TIME_BASED_RANDOM_MIN_LENGTH)
            length = TIME_BASED_RANDOM_MIN_LENGTH;
        return simpleDateFormat.format(new Date()) + random(length - TIME_FORMAT.length());
    }

}
