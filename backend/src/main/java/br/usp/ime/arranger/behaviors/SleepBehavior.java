package br.usp.ime.arranger.behaviors;

public class SleepBehavior extends AbstractBehavior {

    private long millis;

    public SleepBehavior(final long millis) {
        super();
        this.millis = millis;
    }

    @Override
    public void run() throws BehaviorException {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new BehaviorException(e);
        }
    }

    public SleepBehavior() {
        super();
    }

    public long getMillis() {
        return millis;
    }

    public void setMillis(final long millis) {
        this.millis = millis;
    }
}