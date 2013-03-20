package br.usp.ime.arranger;

import java.net.MalformedURLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class Frequency extends AbstractLoadGenerator {

    private FrequencyHelper freqHelper;

    // Requests per minute
    private int minFreq, maxFreq, step;
    // milliseconds
    private int successTime;
    private int maxThreads;
    private float successRatio;

    @Override
    public void generateLoad(final String[] args, final int start)
            throws MalformedURLException, IllegalArgumentException {
        readArgs(args, start);
        freqHelper = new FrequencyHelper(maxThreads);
        setUpClients();
        runSimulations();
    }

    public static String getHelpMessage() {
        return "minFreq maxFreq step successTime successRatio maxThreads";
    }

    private void readArgs(final String[] args, final int start) {
        if (args.length < 6 + start) {
            throw new IllegalArgumentException();
        }

        try {
            minFreq = Integer.parseInt(args[start]);
            maxFreq = Integer.parseInt(args[start + 1]);
            step = Integer.parseInt(args[start + 2]);
            successTime = Integer.parseInt(args[start + 3]);
            successRatio = Float.parseFloat(args[start + 4]);
            maxThreads = Integer.parseInt(args[start + 5]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void setUpClients() {
        FrequencyClient.setup(freqHelper, successTime);
    }

    private void runSimulations() throws MalformedURLException {
        warmUp(minFreq);

        String result;
        for (int frequency = minFreq; frequency <= maxFreq; frequency += step) {
            logTitle(frequency);

            freqHelper.setFrequency(frequency);
            logFrequency();

            runSimulation(freqHelper.getTotalThreads());

            if (hasTimedOut()) {
                result = String.format("result: %d/min", frequency - step);
                CONSOLE.info(result);
                GRAPH.info("# " + result);
                break;
            }
        }
    }

    protected void warmUp(final int freq) throws MalformedURLException {
        CONSOLE.info("warmup");
        GRAPH.info("# warmup");

        freqHelper.setFrequency(freq);
        final int threads = freqHelper.getTotalThreads();

        runSimulation(threads);
    }

    protected void runSimulation(final int threads)
            throws MalformedURLException {
        resetClients();
        runThreads(threads);
    }

    private void logTitle(final int frequency) {
        final String title = String.format("# %d/min", frequency);
        CONSOLE.info(title);
        GRAPH.info(title);
    }

    private void logFrequency() {
        final String msg = String.format("# period=%s, threadPeriod=%s",
                freqHelper.getPeriod(), freqHelper.getThreadPeriod());
        CONSOLE.info(msg);
        GRAPH.info(msg);
    }

    private void resetClients() {
        FrequencyClient.resetStatistics();
        final CountDownLatch threadSync = new CountDownLatch(
                freqHelper.getTotalThreads());
        FrequencyClient.setCountDownLatch(threadSync);
    }

    private boolean hasTimedOut() {
        final int failures = FrequencyClient.getFailures();
        final int successes = FrequencyClient.getSuccesses();
        final float successRatio = ((float) successes) / (successes + failures);

        final String message = String.format("# successes: %s/%s (%.2f%%)",
                successes, (failures + successes), successRatio * 100);
        CONSOLE.info(message);
        GRAPH.info(message);

        return (successRatio < this.successRatio);
    }

    @SuppressWarnings("PMD.WhileLoopsMustUseBraces")
    private void runThreads(final int totalThreads)
            throws MalformedURLException {
        CONSOLE.debug("Using " + totalThreads + " threads");
        final ExecutorService executor = Executors
                .newFixedThreadPool(totalThreads);

        Runnable worker;
        for (int threadNumber = 0; threadNumber < totalThreads; threadNumber++) {
            worker = new FrequencyClient(threadNumber); // NOPMD
            executor.execute(worker);
        }

        executor.shutdown();
        try {
            while (!executor
                    .awaitTermination(THREADS_TIMEOUT, TimeUnit.SECONDS))
                ; // NOPMD
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}