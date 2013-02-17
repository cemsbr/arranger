package br.usp.ime.arranger;

import java.io.UnsupportedEncodingException;

public class StringUtils {

    public int getSizeInBytes(final String string)
            throws UnsupportedEncodingException {
        return string.getBytes("UTF-8").length;
    }

    public String getStringOfLength(final int bytes) {
        final StringBuilder builder = new StringBuilder(bytes);

        for (int i = 0; i < bytes; i++) {
            builder.append('a');
        }

        return builder.toString();
    }
}