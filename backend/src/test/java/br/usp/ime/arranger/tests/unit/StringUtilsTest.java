package br.usp.ime.arranger.tests.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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

	@Test
	public void testStringRandomness() {
		final StringUtils utils = new StringUtils();
		final String actual = utils.getStringOfLength(42);

		final StringBuilder builder = new StringBuilder(actual.length());
		final char firstLetter = actual.charAt(0);
		for (int i = 0; i < actual.length(); i++) {
			builder.append(firstLetter);
		}
		final String sameLetter = builder.toString();

		assertNotEquals(sameLetter, actual);
	}
}