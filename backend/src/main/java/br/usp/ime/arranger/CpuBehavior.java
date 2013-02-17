package br.usp.ime.arranger;

import java.util.Calendar;

public class CpuBehavior extends AbstractBehavior {

    private int milliseconds;
    private int percentage;

    public CpuBehavior(final int milliseconds, final int percentage) {
        super();
        this.milliseconds = milliseconds;
        this.percentage = percentage;
    }

    @Override
    public void run() throws BehaviorException {
        final int period = 100; // NOPMD
        long iteration = 0; // NOPMD
        long iterationStart, iterationEnd;
        final long start = now();
        final long end = start + milliseconds;

        while (now() <= end) {
            iterationStart = start + iteration * period;
            iterationEnd = iterationStart + period;

            while (now() <= iterationStart + percentage) { // NOPMD
                // Busy waiting
            }

            try {
                Thread.sleep(iterationEnd - now());
            } catch (IllegalArgumentException e) { // NOPMD
                // Avoid false alarms with 100 percentage
            } catch (InterruptedException e) {
                throw new BehaviorException(e);
            }

            iteration++;
        }
    }

    private long now() {
        return Calendar.getInstance().getTimeInMillis();
    }

    // Required for serialization

    public CpuBehavior() {
        super();
    }

    public int getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(final int milliseconds) {
        this.milliseconds = milliseconds;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(final int percentage) {
        this.percentage = percentage;
    }
}