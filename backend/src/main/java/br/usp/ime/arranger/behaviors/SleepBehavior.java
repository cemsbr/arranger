package br.usp.ime.arranger.behaviors;

public class SleepBehavior extends AbstractBehavior {

	private long millis;

	public SleepBehavior(final long millis) throws BehaviorException {
		super();
		setMillis(millis);
	}

	public final void setMillis(final long millis) throws BehaviorException {
		if (millis < 0) {
			throw new BehaviorException("Sleep time cannot be negative");
		}
		this.millis = millis;
	}

	@Override
	public void run() throws BehaviorException {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new BehaviorException("Thread.sleep exception", e);
		}
	}

	public SleepBehavior() {
		super();
	}

	public long getMillis() {
		return millis;
	}
}