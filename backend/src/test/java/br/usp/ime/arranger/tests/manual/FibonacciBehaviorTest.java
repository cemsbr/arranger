package br.usp.ime.arranger.tests.manual;

import org.junit.Test;

import br.usp.ime.arranger.behaviors.BehaviorException;
import br.usp.ime.arranger.behaviors.FibonacciBehavior;

public class FibonacciBehaviorTest {

    @Test
    /*
     * Run "top -d 0.5". Press b, x, y. Highlight CPU column with > or <.
     */
    public void manualTest() throws BehaviorException {
        final FibonacciBehavior fibo = new FibonacciBehavior(957000, 1000);
        fibo.run();
    }
}