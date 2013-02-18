package br.usp.ime.arranger.behaviors;

@SuppressWarnings("PMD.ShortVariable")
public class FibonacciBehavior extends AbstractBehavior {

    private long n;
    private long repetitions;

    public FibonacciBehavior(final long n, final long repetitions) {
        super();
        this.n = n;
        this.repetitions = repetitions;
    }

    @Override
    public void run() throws BehaviorException {
        for (int i = 0; i < repetitions; i++) {
            fibonacci();
        }
    }

    private long fibonacci() {
        long result;

        if (n < 2) {
            result = n;
        } else {
            long i = 0;
            long j = 1;
            long aux;
            for (long m = 1; m < n; m++) {
                aux = i;
                i = j;
                j += aux;
            }
            result = j;
        }

        return result;
    }

    // For serialization
    public FibonacciBehavior() {
        super();
    }

    public long getN() {
        return n;
    }

    public final void setN(final long n) {
        this.n = n;
    }

    public long getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(final long repetitions) {
        this.repetitions = repetitions;
    }
}