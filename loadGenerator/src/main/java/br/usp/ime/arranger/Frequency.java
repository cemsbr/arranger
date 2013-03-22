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
    private int maxMean;
    private int minExecs, maxThreads;

    @Override
    public void generateLoad(final String[] args, final int start)
            throws MalformedURLException, IllegalArgumentException {
        readArgs(args, start);
        runSimulations();
    }

    public static String getHelpMessage() {
        return "minFreq maxFreq step minExecs maxMean(milli) maxThreads";
    }

    private void readArgs(final String[] args, final int start) {
        if (args.length < 6 + start) {
            throw new IllegalArgumentException();
        }

        int index = start;
        try {
            minFreq = Integer.parseInt(args[index++]);
            maxFreq = Integer.parseInt(args[index++]);
            step = Integer.parseInt(args[index++]);
            minExecs = Integer.parseInt(args[index++]);
            maxMean = Integer.parseInt(args[index++]);
            maxThreads = Integer.parseInt(args[index++]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void runSimulations() throws MalformedURLException {
        warmUp();

        freqHelper = new FrequencyHelper(maxThreads, minExecs);
        FrequencyClient.setFrequencyHelper(freqHelper);

        for (int frequency = minFreq; frequency <= maxFreq; frequency += step) {
            freqHelper.setFrequency(frequency);
            logFrequency();
            runSimulation();

            if (hasTimedOut()) {
                final String result = String.format("max freq: %d/min",
                        frequency - step);
                CONSOLE.info(result);
                GRAPH.info("# " + result);
                break;
            }
        }
    }

    protected void warmUp() throws MalformedURLException {
        CONSOLE.info("warmup");
        GRAPH.info("# warmup");

        freqHelper = new FrequencyHelper(maxThreads, minFreq, minFreq);
        FrequencyClient.setFrequencyHelper(freqHelper);
        runSimulation();
    }

    protected void runSimulation() throws MalformedURLException {
        resetClients();
        runThreads();
    }

    private void logFrequency() {
        final int freq = freqHelper.getFrequency();
        final double period = freqHelper.getPeriod();
        final double tPeriod = freqHelper.getThreadPeriod();

        final String title = String.format(
                "# %d/min, period=%s, threadPeriod=%s", freq, period, tPeriod);
        CONSOLE.info(title);
        GRAPH.info(title);
    }

    private void resetClients() {
        FrequencyClient.resetStatistics();
        final CountDownLatch threadSync = new CountDownLatch(
                freqHelper.getTotalThreads());
        FrequencyClient.setCountDownLatch(threadSync);
    }

    private boolean hasTimedOut() {
        final double mean = FrequencyClient.getMean();
        final String message = "# mean = " + mean;
        CONSOLE.info(message);
        GRAPH.info(message);

        return (mean > maxMean);
    }

    @SuppressWarnings("PMD.WhileLoopsMustUseBraces")
    private void runThreads() throws MalformedURLException {
        final int totalThreads = freqHelper.getTotalThreads();
        CONSOLE.debug("Using " + totalThreads + " threads");

        final ExecutorService executor = Executors
                .newFixedThreadPool(totalThreads);
        createWorkers(totalThreads, executor);
        executor.shutdown();

        try {
            while (!executor
                    .awaitTermination(THREADS_TIMEOUT, TimeUnit.SECONDS))
                ; // NOPMD
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }

    private void createWorkers(final int totalThreads,
            final ExecutorService executor) throws MalformedURLException {
        Runnable worker;
        for (int threadNumber = 0; threadNumber < totalThreads; threadNumber++) {
            worker = new FrequencyClient(threadNumber); // NOPMD
            executor.execute(worker);
        }
    }
}
