package br.usp.ime.arranger.tests.manual;

import org.junit.Test;

import br.usp.ime.arranger.BehaviorException;
import br.usp.ime.arranger.CpuBehavior;

public class CpuBehaviorTest {

    @Test
    /*
     * Run "top -d 0.5". Press b, x, y. Highlight CPU column with > or <.
     */
    public void manualTest() throws BehaviorException {
        final CpuBehavior cpu = new CpuBehavior(10000, 50);
        cpu.run();
    }
}