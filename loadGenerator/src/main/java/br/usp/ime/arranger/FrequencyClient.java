package br.usp.ime.arranger;

import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import br.usp.ime.arranger.behaviors.BehaviorException;

public class FrequencyClient extends AbstractClient implements Runnable {

    private static FrequencyHelper freqHelper = null;

    private static AtomicInteger successes;
    private static AtomicInteger failures;
    private static double successTime = -1;

    /**
     * Must set FrequencyHelper and SuccessTime before.
     * 
     * @param threadNumber
     * @throws MalformedURLException
     */
    public FrequencyClient(final int threadNumber) throws MalformedURLException {
        super(threadNumber);

    }

    public static void setFrequencyHelper(final FrequencyHelper freqHelper) {
        FrequencyClient.freqHelper = freqHelper;
    }

    public static void setSuccessTime(final int successTime) {
        FrequencyClient.successTime = successTime;
    }

    public static void resetStatistics() {
        successes = new AtomicInteger(0);
        failures = new AtomicInteger(0);
    }

    public static int getSuccesses() {
        return successes.get();
    }

    public static int getFailures() {
        return failures.get();
    }

    @Override
    public void run() throws IllegalStateException {
        if (freqHelper == null || successTime < 0) {
            throw new IllegalStateException(
                    "Must set FrequencyHelper and successTime in FrequencyClient");
        }

        final int requests = freqHelper.getTotalRequests(threadNumber);
        waitOtherThreads();
        freqHelper.setStartTime();

        for (int i = 0; i < requests; i++) {
            try {
                if (!canWait(i)) {
                    continue;
                }
            } catch (InterruptedException e) {
                logError("Sleep < 0");
            }

            try {
                simulate();
            } catch (BehaviorException e) {
                logError("simulate()", e);
                failures.incrementAndGet();
            }
        }
    }

    private boolean canWait(final int request) throws InterruptedException {
        boolean canWait = true; // NOPMD - time restriction

        final long sleepTime = freqHelper.getSleepTime(threadNumber, request);
        if (sleepTime < 0) {
            logError(String.format("Sleep time %d on thread %d, request %d.",
                    sleepTime, threadNumber, request));
            failures.incrementAndGet();
            canWait = false;
        } else {
            Thread.sleep(sleepTime);
        }

        return canWait;
    }

    private void waitOtherThreads() {
        latch.countDown();
        try {
            latch.await();
        } catch (InterruptedException e1) {
            logError("CountDownLatch await error", e1);
        }
    }

    @Override
    protected void simulate() throws BehaviorException {
        final long start = Calendar.getInstance().getTimeInMillis();

        server.run();

        final long duration = Calendar.getInstance().getTimeInMillis() - start;
        GRAPH.info(start + "," + duration);
        checkTime(duration);
    }

    private void checkTime(final long duration) {
        if (duration > successTime) {
            failures.incrementAndGet();
        } else {
            successes.incrementAndGet();
        }
    }
}