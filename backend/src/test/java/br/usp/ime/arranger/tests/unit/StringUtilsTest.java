package br.usp.ime.arranger.tests.unit;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import br.usp.ime.arranger.behaviors.BehaviorException;
import br.usp.ime.arranger.utils.StringUtils;

public class StringUtilsTest {

    @Test
    public void testMessageSize() throws UnsupportedEncodingException,
            BehaviorException {
        final StringUtils utils = new StringUtils();
        final String tenBytes = utils.getStringOfLength(10);
        assertEquals(10, utils.getSizeInBytes(tenBytes));
    }
}