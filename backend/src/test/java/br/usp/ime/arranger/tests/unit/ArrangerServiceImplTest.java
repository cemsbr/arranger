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
import org.junit.Test;

import br.usp.ime.arranger.Behavior;
import br.usp.ime.arranger.BehaviorException;
import br.usp.ime.arranger.CpuBehavior;
import br.usp.ime.arranger.StringUtils;
import br.usp.ime.arranger.service.Arranger;
import br.usp.ime.arranger.service.ArrangerImpl;

public class ArrangerServiceImplTest {

    private Arranger service;
    private final Behavior cpu1 = new CpuBehavior(1000, 10);
    private final Behavior cpu2 = new CpuBehavior(2000, 20);
    private List<Behavior> beSent, beReceived;

    @Before
    public void setUp() {
        service = new ArrangerImpl();
        beSent = new ArrayList<>();
    }

    @Test
    public void shouldBeginWithNoBehaviors() {
        beReceived = service.getBehaviors();
        assertEquals(0, beReceived.size());
    }

    @Test
    public void shouldSaveABehavior() {
        service.setBehavior(cpu1);
        beReceived = service.getBehaviors();

        assertEquals(cpu1, beReceived.get(0));
    }

    @Test
    public void shouldPreserveBehaviorsOrder() {
        beSent.add(cpu1);
        beSent.add(cpu2);
        service.setBehaviors(beSent);
        assertEquals(beSent, service.getBehaviors());

        beSent.clear();
        beSent.add(cpu2);
        beSent.add(cpu1);
        service.setBehaviors(beSent);
        assertEquals(beSent, service.getBehaviors());
    }

    @Test
    public void shouldRunAllBehaviorsOnRun() throws BehaviorException {
        final Behavior beMock1 = mock(CpuBehavior.class);
        final Behavior beMock2 = mock(CpuBehavior.class);

        beSent.add(beMock1);
        beSent.add(beMock2);
        service.setBehaviors(beSent);
        service.run();

        verify(beMock1).run();
        verify(beMock2).run();
    }

    @Test
    public void shouldCallRunWhenInReceiveMessage() throws BehaviorException {
        final Arranger spy = spy(service);
        spy.exchangeMessages("42", 0);
        verify(spy).run();
    }

    @Test
    public void shouldClearBehaviorAfterSettingNewBehavior() {
        final Behavior cpu1Spy = spy(cpu1);

        service.setBehavior(cpu1Spy);
        verify(cpu1Spy, never()).clear();

        service.setBehavior(cpu2);
        verify(cpu1Spy, times(1)).clear();
    }

    @Test
    public void shouldClearBehaviorAfterSettingNewBehaviors() {
        final Behavior cpu1Spy = spy(cpu1);
        final Behavior cpu2Spy = spy(cpu2);
        final List<Behavior> beSentSpy = new ArrayList<>(2);
        beSentSpy.add(cpu1Spy);
        beSentSpy.add(cpu2Spy);

        service.setBehaviors(beSentSpy);
        verify(cpu1Spy, never()).clear();
        verify(cpu2Spy, never()).clear();

        beSent.add(cpu1);
        beSent.add(cpu2);
        service.setBehaviors(beSent);
        verify(cpu1Spy, times(1)).clear();
        verify(cpu2Spy, times(1)).clear();
    }
    
    @Test
    public void testReturnSizeWhenExchangingMessages()
            throws BehaviorException, UnsupportedEncodingException {
        final int size = 42;
        final String response = service.exchangeMessages("message", size);
        final StringUtils strUtils = new StringUtils();
        assertEquals(size, strUtils.getSizeInBytes(response));
    }
}