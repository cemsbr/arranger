package br.usp.ime.arranger.tests.unit;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.usp.ime.arranger.behaviors.BehaviorException;
import br.usp.ime.arranger.behaviors.MemoryBehavior;
import br.usp.ime.tests.utils.ReflectionUtils;

public class MemoryBehaviorTest {

	private transient MemoryBehavior memory;
	private transient int size;

	private static Method changeSignMethod, genArrayMethod;

	@BeforeClass
	public static void setUpClass() throws NoSuchMethodException,
			SecurityException {
		changeSignMethod = ReflectionUtils
				.setMethodPublic(MemoryBehavior.class, "changeSign",
						new Class[]{double[].class});
		genArrayMethod = ReflectionUtils.setMethodPublic(MemoryBehavior.class,
				"generateRandomArray");
	}

	@Before
	public void setUp() {
		memory = new MemoryBehavior();
	}

	@Test
	public void shouldGenerateRandomArrayWithSize0() throws BehaviorException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		createRandomArrayWithSize(0);
	}

	@Test(expected = BehaviorException.class)
	public void shouldThrowExceptionWithNegativeSize()
			throws BehaviorException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		createRandomArrayWithSize(-1);
	}

	@Test
	public void shouldGenerateRandomArrayWithSize5() throws BehaviorException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		createRandomArrayWithSize(5);
	}

	@Test
	public void shouldPreserveSizeAfterChangingSign() throws BehaviorException,
			NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		size = 5;
		memory.setSize(size);

		final double[] numbers = (double[]) genArrayMethod.invoke(memory);
		changeSignMethod.invoke(memory, numbers);

		assertEquals(size, numbers.length);
	}

	@Test
	public void shouldChangeSign() throws BehaviorException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		size = 5;
		memory.setSize(size);
		final double[] numbers = (double[]) genArrayMethod.invoke(memory);
		final double[] oldNumbers = new double[numbers.length];
		System.arraycopy(numbers, 0, oldNumbers, 0, numbers.length);

		changeSignMethod.invoke(memory, numbers);

		for (int i = 0; i < size; i++) {
			assertEquals(-1 * oldNumbers[i], numbers[i], 0.00001);
		}
	}

	private void createRandomArrayWithSize(final int size)
			throws BehaviorException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		memory.setSize(size);
		final double[] numbers = (double[]) genArrayMethod.invoke(memory);
		assertEquals(size, numbers.length);
	}

	@Test
	public void testMemoryUsed() throws BehaviorException {
		memory.setSize(2000000);
		memory.run();
	}
}