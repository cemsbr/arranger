package br.usp.ime.futuremarket.choreography;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;

import org.junit.BeforeClass;
import org.junit.Test;

import br.usp.ime.arranger.FrequencyHelper;

public class FrequencyHelperTest {

	private static Calendar calendar;
	private long startTime;
	private static Method pvtMethod;
	private FrequencyHelper helper;

	@BeforeClass
	public static void setCalendarInstance() throws NoSuchMethodException,
			SecurityException {
		calendar = Calendar.getInstance();
		pvtMethod = FrequencyHelper.class.getDeclaredMethod("getEventTime",
				Integer.TYPE, Integer.TYPE);
		pvtMethod.setAccessible(true);
	}

	private long getTime() {
		return calendar.getTimeInMillis();
	}

	/*-
	 * 1000/min, 100 threads. Solution:
	 * t00: 00000, 06000, 12000, 18000, ..., 54000
	 * t01: 00060, 06060, 12060, 18060, ..., 54060 (10)
	 * ...
	 * t99: 05940, 11940, 17940, 23940, ..., 59940 (10)
	 */
	@Test
	public void case1Test() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		helper = new FrequencyHelper(100, 1000, 1000);

		assertEquals(100, helper.getTotalThreads());

		setStartTime();

		assertEquals(startTime, getEventTime(0, 0));
		assertEquals(startTime + 6000, getEventTime(0, 1));
		assertEquals(startTime + 54000, getEventTime(0, 9));

		assertEquals(startTime + 60, getEventTime(1, 0));
		assertEquals(startTime + 6060, getEventTime(1, 1));
		assertEquals(startTime + 54060, getEventTime(1, 9));

		assertEquals(startTime + 5940, getEventTime(99, 0));
		assertEquals(startTime + 11940, getEventTime(99, 1));
		assertEquals(startTime + 59940, getEventTime(99, 9));

		assertEquals(10, helper.getTotalRequests(0));
		assertEquals(10, helper.getTotalRequests(1));
		assertEquals(10, helper.getTotalRequests(2));
	}

	private void setStartTime() {
		startTime = getTime();
		helper.setStartTime(startTime);
		startTime += FrequencyHelper.DELAY;
	}

	/*-
	 * 4/min, 1 thread. Solution:
	 * t1: 0, 15k, 30k, 45k
	 */
	@Test
	public void case2aTest() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		helper = new FrequencyHelper(1, 4, 4);

		assertEquals(1, helper.getTotalThreads());

		setStartTime();

		assertEquals(startTime, getEventTime(0, 0));
		assertEquals(startTime + 15000, getEventTime(0, 1));
		assertEquals(startTime + 30000, getEventTime(0, 2));
		assertEquals(startTime + 45000, getEventTime(0, 3));

		assertEquals(4, helper.getTotalRequests(0));
	}

	/*-
	 * 4/min, 2 threads. Solution:
	 * t1: 0, 30k
	 * t2: 15k, 45k
	 */
	@Test
	public void case2bTest() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		helper = new FrequencyHelper(2, 4, 4);

		assertEquals(2, helper.getTotalThreads());

		setStartTime();

		assertEquals(startTime, getEventTime(0, 0));
		assertEquals(startTime + 30000, getEventTime(0, 1));

		assertEquals(startTime + 15000, getEventTime(1, 0));
		assertEquals(startTime + 45000, getEventTime(1, 1));

		assertEquals(2, helper.getTotalRequests(0));
		assertEquals(2, helper.getTotalRequests(1));
	}

	/*-
	 * 4/min, 3 threads. Solution:
	 * t1: 0, 45k
	 * t2: 15k
	 * t3: 30k
	 */
	@Test
	public void case2cTest() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		helper = new FrequencyHelper(3, 4, 4);

		assertEquals(3, helper.getTotalThreads());

		setStartTime();

		assertEquals(startTime, getEventTime(0, 0));
		assertEquals(startTime + 45000, getEventTime(0, 1));

		assertEquals(startTime + 15000, getEventTime(1, 0));

		assertEquals(startTime + 30000, getEventTime(2, 0));

		assertEquals(2, helper.getTotalRequests(0));
		assertEquals(1, helper.getTotalRequests(1));
		assertEquals(1, helper.getTotalRequests(2));
	}

	/*-
	 * 4/min, 4 threads. Solution:
	 * t1: 0
	 * t2: 15k
	 * t3: 30k
	 * t4: 45k
	 */
	@Test
	public void case2dTest() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		helper = new FrequencyHelper(4, 4, 4);

		assertEquals(4, helper.getTotalThreads());

		setStartTime();

		assertEquals(startTime, getEventTime(0, 0));
		assertEquals(startTime + 15000, getEventTime(1, 0));
		assertEquals(startTime + 30000, getEventTime(2, 0));
		assertEquals(startTime + 45000, getEventTime(3, 0));

		for (int i = 0; i < 4; i++) {
			assertEquals(1, helper.getTotalRequests(i));
		}
	}

	/*-
	 * 4/min, 5 threads. Solution:
	 * t1: 0k
	 * t2: 15k
	 * t3: 30k
	 * t4: 45k
	 */
	@Test
	public void case2eTest() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		helper = new FrequencyHelper(5, 4, 4);

		assertEquals(4, helper.getTotalThreads());

		setStartTime();

		assertEquals(startTime, getEventTime(0, 0));
		assertEquals(startTime + 15000, getEventTime(1, 0));
		assertEquals(startTime + 30000, getEventTime(2, 0));
		assertEquals(startTime + 45000, getEventTime(3, 0));

		for (int i = 0; i < 4; i++) {
			assertEquals(1, helper.getTotalRequests(i));
		}
	}

	@Test
	public void shouldRoundNumbersCorrectly() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		helper = new FrequencyHelper(800, 14, 14);

		assertEquals(14, helper.getTotalThreads());

		setStartTime();

		assertEquals(startTime + 4286, getEventTime(1, 0));
		assertEquals(startTime + 8571, getEventTime(2, 0));
		assertEquals(startTime + 60000, getEventTime(14, 0));
	}

	@Test
	public void testTotalRequestsAfterOneMinute() {
		helper = new FrequencyHelper(800, 1, 2);
		assertEquals(1, helper.getTotalRequests(0));
		assertEquals(1, helper.getTotalRequests(1));
	}

	@Test
	public void testEventTimeAfterOneMinute() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		helper = new FrequencyHelper(800, 1, 2);
		setStartTime();

		assertEquals(startTime, getEventTime(0, 0));
		assertEquals(startTime + 60000, getEventTime(1, 0));
	}

	@Test
	public void testFrequencyHigherThanExecutions() {
		helper = new FrequencyHelper(500, 100, 30);

		assertEquals(100, helper.getTotalThreads());

		assertEquals(1, helper.getTotalRequests(0));
		assertEquals(1, helper.getTotalRequests(99));
	}

	private long getEventTime(final int threadNumber, final int iteration)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		return (long) pvtMethod.invoke(helper, threadNumber, iteration);
	}
}