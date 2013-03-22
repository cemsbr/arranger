package br.usp.ime.arranger.utils;

/**
 * Thread-safe-overflow-proof arithmetic mean calculator.
 * 
 * @author cadu
 */
public class ArithmeticMean {

    private double n; // NOPMD
    private double mean;

    public ArithmeticMean() {
        reset();
    }

    public void add(final long value) {
        synchronized (this) {
            n++;
            mean = ((n - 1) / n) * mean + value / n;
        }
    }

    public final void reset() {
        synchronized (this) {
            n = 0;
            mean = 0;
        }
    }

    public double getMean() {
        return mean;
    }
}