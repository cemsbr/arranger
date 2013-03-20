package br.usp.ime.arranger;

import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import br.usp.ime.arranger.behaviors.BehaviorException;

public class FrequencyClient extends AbstractClient implements Runnable {

    private static FrequencyHelper freqHelper;

    private static AtomicInteger successes;
    private static AtomicInteger failures;
    private static double successTime;

    public static void setup(final FrequencyHelper freqHelper,
            final int successTime) {
        FrequencyClient.freqHelper = freqHelper;
        FrequencyClient.successTime = successTime;
    }

    public FrequencyClient(final int threadNumber) throws MalformedURLException {
        super(threadNumber);
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
    public void run() {
        final int requests = freqHelper.getTotalRequests(threadNumber);

        latch.countDown();
        try {
            latch.await();
        } catch (InterruptedException e1) {
            logError("CountDownLatch await error", e1);
        }

        freqHelper.setStartTime();

        for (int i = 0; i < requests; i++) {
            try {
                final long sleepTime = freqHelper.getSleepTime(threadNumber, i);
                if (sleepTime < 0) {
                    logError(String.format(
                            "Sleep time %d on thread %d, request %d.",
                            sleepTime, threadNumber, i));
                    failures.incrementAndGet();
                    continue;
                }
                Thread.sleep(sleepTime);
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