package br.usp.ime.arranger.behaviors;

import java.util.Random;

public class MemoryBehavior extends AbstractBehavior {

	private int size;

	public MemoryBehavior(final int size) throws BehaviorException {
		super();
		setSize(size);
	}

	@Override
	public void run() throws BehaviorException {
		final double[] randomFloats = generateRandomArray();
		changeSign(randomFloats);
	}

	public final void setSize(final int size) throws BehaviorException {
		if (size < 0) {
			throw new BehaviorException(
					"MemoryBehavior cannot have negative size");
		}
		this.size = size;
	}

	public int getSize() {
		return size;
	}

	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	private double[] generateRandomArray() throws BehaviorException {
		final double[] numbers = new double[size];
		final Random random = new Random();

		for (int i = 0; i < size; i++) {
			numbers[i] = random.nextDouble();
		}

		return numbers;
	}

	private void changeSign(final double[] numbers) {
		for (int i = 0; i < size; i++) {
			numbers[i] *= -1;
		}
	}

	// Needed for serialization
	public MemoryBehavior() {
		super();
	}
}