package br.usp.ime.arranger.utils;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class StringUtils {

    private static final char[] SUBSET = "0123456789abcdefghijklmnopqrstuvwxyz"
            .toCharArray();

    public int getSizeInBytes(final String string)
            throws UnsupportedEncodingException {
        return string.getBytes("UTF-8").length;
    }

    public String getStringOfLength(final int bytes) {
        final Random random = new Random();
        final char[] buffer = new char[bytes];
        for (int i = 0; i < bytes; i++) {
            buffer[i] = SUBSET[random.nextInt(SUBSET.length)];
        }
        return new String(buffer);
    }
}