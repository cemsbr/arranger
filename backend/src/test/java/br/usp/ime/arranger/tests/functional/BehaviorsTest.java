package br.usp.ime.arranger.tests.functional;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.util.List;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import br.usp.ime.arranger.behaviors.Behavior;
import br.usp.ime.arranger.behaviors.BehaviorException;
import br.usp.ime.arranger.behaviors.FibonacciBehavior;
import br.usp.ime.arranger.behaviors.MemoryBehavior;
import br.usp.ime.arranger.behaviors.SleepBehavior;
import br.usp.ime.arranger.service.MessageBehavior;
import br.usp.ime.arranger.service.Performer;
import br.usp.ime.arranger.service.PerformerProxyCreator;

@SuppressWarnings("PMD.AvoidFinalLocalVariable")
public class BehaviorsTest {

    private static PerformerPublisher publisher;
    private static PerformerProxyCreator proxyCreator;
    private transient Performer webService;
    private transient Behavior expected;

    @BeforeClass
    public static void setUpClass() {
        publisher = new PerformerPublisher();
        proxyCreator = new PerformerProxyCreator();
    }

    @After
    public void tearDown() {
        publisher.stopAll();
    }

    @Test
    public void shouldSetFibonnaciBehavior() throws MalformedURLException,
            BehaviorException {
        final int n = 1; // NOPMD
        final int repetitions = 2;
        expected = new FibonacciBehavior(n, repetitions);

        publishAndSetBehavior(expected);
        final FibonacciBehavior actual = (FibonacciBehavior) getFirstBehavior();

        assertEquals(n, actual.getN());
        assertEquals(repetitions, actual.getRepetitions());
    }

    @Test
    public void shouldSetMemoryBehavior() throws MalformedURLException,
            BehaviorException {
        final int size = 100;
        expected = new MemoryBehavior(size);

        publishAndSetBehavior(expected);
        final MemoryBehavior actual = (MemoryBehavior) getFirstBehavior();

        assertEquals(size, actual.getSize());
    }

    @Test
    public void shoulSetMessageBehavior() throws BehaviorException,
            MalformedURLException {
        final String wsdl = publisher.publish();
        final long request = 1;
        final long response = 2;

        final Behavior expected = new MessageBehavior(wsdl, request, response);
        webService = proxyCreator.getProxy(wsdl);
        webService.setBehavior(expected);

        final MessageBehavior actual = (MessageBehavior) getFirstBehavior();

        assertEquals(wsdl, actual.getDestWsdl());
        assertEquals(request, actual.getRequestBytes());
        assertEquals(response, actual.getResponseBytes());
    }

    @Test
    public void shouldSetSleepBehavior() throws Exception {
        final long time = 42;
        expected = new SleepBehavior(42);

        publishAndSetBehavior(expected);
        final SleepBehavior actual = (SleepBehavior) getFirstBehavior();

        assertEquals(time, actual.getMillis());
    }

    private Behavior getFirstBehavior() {
        final List<Behavior> behaviors = webService.getBehaviors();
        return behaviors.get(0);
    }

    private void publishAndSetBehavior(final Behavior behavior)
            throws MalformedURLException, BehaviorException {
        webService = publish();
        webService.setBehavior(behavior);
    }

    private Performer publish() throws MalformedURLException {
        final String wsdl = publisher.publish();
        return proxyCreator.getProxy(wsdl);
    }
}