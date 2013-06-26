package br.usp.ime.arranger.tests.unit;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.BeforeClass;
import org.junit.Test;

import br.usp.ime.arranger.behaviors.FibonacciBehavior;

@SuppressWarnings("PMD.ShortVariable")
public class FibonacciBehaviorTest {

	private static Method fibo;

	@BeforeClass
	public static void makeMethodPublic() throws NoSuchMethodException,
			SecurityException {
		fibo = FibonacciBehavior.class.getDeclaredMethod("fibonacci");
		fibo.setAccessible(true);
	}

	@Test
	public void testFirstNumber() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		final long n = (long) fibo.invoke(new FibonacciBehavior(0, 1));
		assertEquals(0, n);
	}

	@Test
	public void testSecondNumber() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		final long n = (long) fibo.invoke(new FibonacciBehavior(1, 1));
		assertEquals(1, n);
	}

	@Test
	public void testThirdToTenthNumbers() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		final long[] expected = {1, 2, 3, 5, 8, 13, 21, 34, 55};
		long actual;
		for (int n = 2; n < 10; n++) {
			actual = (long) fibo.invoke(new FibonacciBehavior(n, 1)); // NOPMD
			assertEquals(expected[n - 2], actual);
		}
	}

	@Test
	public void testBiggestLongResult() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		final long actual = (long) fibo.invoke(new FibonacciBehavior(92, 1));
		assertEquals(7540113804746346429L, actual);
	}
}