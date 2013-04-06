package br.usp.ime.arranger.tests.unit;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Test;

import br.usp.ime.arranger.behaviors.Behavior;
import br.usp.ime.arranger.behaviors.BehaviorException;
import br.usp.ime.arranger.behaviors.SleepBehavior;

public class SleepBehaviorTest {

    @Test
    public void shouldSleep50ms() throws BehaviorException {
        assertSleepTime(50);
    }

    @Test
    public void shouldSleep100ms() throws BehaviorException {
        assertSleepTime(100);
    }

    @Test(expected = BehaviorException.class)
    public void shouldThrowBehaviorExceptionIfTimeIsNegative()
            throws BehaviorException {
        new SleepBehavior(-1);
    }

    private void assertSleepTime(final long milli) throws BehaviorException {
        final long duration = sleep(milli);
        assertTrue(duration >= milli);
    }

    private long sleep(final long milli) throws BehaviorException {
        final Behavior sleep = new SleepBehavior(milli);
        final long start = Calendar.getInstance().getTimeInMillis();
        sleep.run();
        return Calendar.getInstance().getTimeInMillis() - start;
    }
}