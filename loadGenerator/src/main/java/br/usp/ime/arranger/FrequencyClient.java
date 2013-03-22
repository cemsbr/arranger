package br.usp.ime.arranger;

import java.net.MalformedURLException;
import java.util.Calendar;

import br.usp.ime.arranger.behaviors.BehaviorException;
import br.usp.ime.arranger.utils.ArithmeticMean;

public class FrequencyClient extends AbstractClient implements Runnable {

    private static FrequencyHelper freqHelper = null;
    private static final ArithmeticMean MEAN = new ArithmeticMean();

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

    public static double getMean() {
        return MEAN.getMean();
    }

    public static void resetStatistics() {
        MEAN.reset();
    }

    @Override
    public void run() throws IllegalStateException {
        if (freqHelper == null) {
            throw new IllegalStateException("FrequencyHelper must be set");
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
            }
        }
    }

    private boolean canWait(final int request) throws InterruptedException {
        boolean canWait = true; // NOPMD - time restriction

        final long sleepTime = freqHelper.getSleepTime(threadNumber, request);
        if (sleepTime < 0) {
            logError(String.format("Sleep time %d on thread %d, request %d.",
                    sleepTime, threadNumber, request));
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
        MEAN.add(duration);
    }
}