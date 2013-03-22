package br.usp.ime.arranger;

import java.util.Calendar;

/**
 * This class helps to calculate when to trigger events in order to match a
 * given frequency. The required time is one minute and there is support to
 * multiple instances (e.g.: load generator threads).
 * 
 * @author Cadu
 * 
 */
public class FrequencyHelper {
    // period, threadPeriod are in milliseconds.
    private int maxThreads, executions;
    private double period, threadPeriod;
    private int frequency, totalThreads;
    private long startTime;

    public static final int DELAY = 100;

    /**
     * 
     * @param maxThreads
     * @param frequency
     *            requests per minute.
     * @param executions
     *            total number of requests.
     */
    public FrequencyHelper(final int maxThreads, final int frequency,
            final int executions) {
        this(maxThreads, executions);
        setFrequency(frequency); // NOPMD
    }

    /**
     * 
     * @param maxThreads
     *            total number of requests.
     * @param executions
     */
    public FrequencyHelper(final int maxThreads, final int executions) {
        this.executions = executions;
        this.maxThreads = maxThreads;
    }

    /**
     * 
     * @param frequency
     *            requests per minute.
     */
    public void setFrequency(final int frequency) {
        this.frequency = frequency;

        totalThreads = Math.min(maxThreads, executions);
        period = 60000.0 / frequency;
        threadPeriod = totalThreads * period;
    }

    public void setStartTime() {
        setStartTime(getCurrentTime());
    }

    public void setExecutions(final int executions) {
        this.executions = executions;
    }

    public long getSleepTime(final int threadNumber, final int iteration) {
        final long eventTime = getEventTime(threadNumber, iteration);
        return Math.round(eventTime - getCurrentTime());
    }

    private long getEventTime(final int threadNumber, final int iteration) {
        return startTime + getEventDelay(threadNumber, iteration) + DELAY;
    }

    private long getCurrentTime() {
        return Calendar.getInstance().getTimeInMillis();
    }

    private long getEventDelay(final int threadNumber, final int iteration) {
        final double firstThreadEvent = threadNumber * period;
        final double iterationDelay = iteration * threadPeriod;
        return Math.round(firstThreadEvent + iterationDelay);
    }

    public int getTotalRequests(final int threadNumber) {
        double requests;
        if (threadNumber < executions % totalThreads) {
            requests = executions / totalThreads + 1;
        } else {
            requests = executions / totalThreads;
        }
        return (int) Math.round(requests);
    }

    public void setStartTime(final long startTime) {
        this.startTime = startTime;
    }

    public int getFrequency() {
        return frequency;
    }

    public double getPeriod() {
        return period;
    }

    public double getThreadPeriod() {
        return threadPeriod;
    }

    public int getTotalThreads() {
        return totalThreads;
    }
}