package br.usp.ime.arranger;

import java.net.MalformedURLException;
import java.util.Calendar;

import br.usp.ime.arranger.behaviors.BehaviorException;

public class SimultaneousClient extends AbstractClient implements Runnable {

	public SimultaneousClient(final int threadNumber)
			throws MalformedURLException {
		super(threadNumber);
	}

	@Override
	public void run() {
		latch.countDown();
		try {
			latch.await();
		} catch (InterruptedException e1) {
			logError("CountDownLatch await error", e1);
		}

		try {
			simulate();
		} catch (BehaviorException e) {
			logError("simulate()", e);
		}
	}

	@Override
	protected void simulate() throws BehaviorException {
		final long start = Calendar.getInstance().getTimeInMillis();

		server.run();

		final long end = Calendar.getInstance().getTimeInMillis();
		GRAPH.info(end - start);
	}
}