package br.usp.ime.arranger.tests.unit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.usp.ime.arranger.behaviors.Behavior;
import br.usp.ime.arranger.behaviors.BehaviorException;
import br.usp.ime.arranger.behaviors.SleepBehavior;
import br.usp.ime.arranger.service.Performer;
import br.usp.ime.arranger.service.PerformerImpl;
import br.usp.ime.arranger.utils.StringUtils;

public class PerformerImplTest {

    private Performer service;
    private static Behavior sleep1;
    private static Behavior sleep2;
    private List<Behavior> beSent, beReceived;

    @BeforeClass
    public static void setUpClass() throws BehaviorException {
        sleep1 = new SleepBehavior(1000);
        sleep2 = new SleepBehavior(2000);
    }

    @Before
    public void setUp() {
        service = new PerformerImpl();
        beSent = new ArrayList<>();
    }

    @Test
    public void shouldBeginWithNoBehaviors() {
        beReceived = service.getBehaviors();
        assertEquals(0, beReceived.size());
    }

    @Test
    public void shouldSaveABehavior() throws BehaviorException {
        service.setBehavior(sleep1);
        beReceived = service.getBehaviors();

        assertEquals(sleep1, beReceived.get(0));
    }

    @Test
    public void shouldPreserveBehaviorsOrder() throws BehaviorException {
        beSent.add(sleep1);
        beSent.add(sleep2);
        service.setBehaviors(beSent);
        assertEquals(beSent, service.getBehaviors());

        beSent.clear();
        beSent.add(sleep2);
        beSent.add(sleep1);
        service.setBehaviors(beSent);

        beReceived = service.getBehaviors();
        assertEquals(beSent.get(0), beReceived.get(0));
        assertEquals(beSent.get(1), beReceived.get(1));
    }

    @Test
    public void shouldRunAllBehaviorsOnRun() throws BehaviorException {
        final Behavior beMock1 = mock(SleepBehavior.class);
        final Behavior beMock2 = mock(SleepBehavior.class);

        beSent.add(beMock1);
        beSent.add(beMock2);
        service.setBehaviors(beSent);
        service.run();

        verify(beMock1).run();
        verify(beMock2).run();
    }

    @Test
    public void shouldCallRunWhenInReceiveMessage() throws BehaviorException {
        final Performer spy = spy(service);
        spy.msgStringReqStringRes("42", 0);
        verify(spy).run();
    }

    @Test
    public void shouldClearBehaviorAfterSettingNewBehavior()
            throws BehaviorException {
        final Behavior cpu1Spy = spy(sleep1);

        service.setBehavior(cpu1Spy);
        verify(cpu1Spy, never()).destroy();

        service.setBehavior(sleep2);
        verify(cpu1Spy, times(1)).destroy();
    }

    @Test
    public void shouldClearBehaviorAfterSettingNewBehaviors()
            throws BehaviorException {
        final Behavior cpu1Spy = spy(sleep1);
        final Behavior cpu2Spy = spy(sleep2);
        final List<Behavior> beSentSpy = new ArrayList<>(2);
        beSentSpy.add(cpu1Spy);
        beSentSpy.add(cpu2Spy);

        service.setBehaviors(beSentSpy);
        verify(cpu1Spy, never()).destroy();
        verify(cpu2Spy, never()).destroy();

        beSent.add(sleep1);
        beSent.add(sleep2);
        service.setBehaviors(beSent);
        verify(cpu1Spy, times(1)).destroy();
        verify(cpu2Spy, times(1)).destroy();
    }

    @Test
    public void testReturnSizeWhenExchangingMessages()
            throws BehaviorException, UnsupportedEncodingException {
        final int size = 42;
        final String response = service.msgStringReqStringRes("message", size);
        final StringUtils strUtils = new StringUtils();
        assertEquals(size, strUtils.getSizeInBytes(response));
    }
}