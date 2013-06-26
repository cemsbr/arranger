package br.usp.ime.arranger.tests.functional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import br.usp.ime.arranger.behaviors.Behavior;
import br.usp.ime.arranger.behaviors.BehaviorException;
import br.usp.ime.arranger.service.Performer;
import br.usp.ime.arranger.service.PerformerImpl;
import br.usp.ime.arranger.utils.CommUtils;

public class BehaviorExceptionTest {

	private transient PerformerPublisher publisher;
	private transient CommUtils comm;

	@Before
	public void setUp() {
		publisher = new PerformerPublisher();
		comm = new CommUtils();
	}

	@After
	public void tearDown() {
		publisher.stopAll();
		CommUtils.destroy();
	}

	@Test(expected = BehaviorException.class)
	public void shouldCatchBehaviorException() throws MalformedURLException,
			BehaviorException {
		final Performer performer = getPerformer(new BehaviorException());
		final String wsdl = publisher.publish(performer);
		final Performer proxy = comm.getPerformer(wsdl);

		proxy.run();
	}

	@Test
	public void shouldReadBehaviorExceptionMessage() throws BehaviorException,
			MalformedURLException {
		final String msg = "The answer is 42.";
		final Performer performer = getPerformer(new BehaviorException(msg));

		final String wsdl = publisher.publish(performer);
		final Performer proxy = comm.getPerformer(wsdl);

		try {
			proxy.run();
			fail();
		} catch (BehaviorException e) {
			assertEquals(msg, e.getMessage());
		}
	}

	private Performer getPerformer(final Exception behaviorException)
			throws BehaviorException {
		final Performer performer = new PerformerImpl();
		final Behavior mockedBehavior = Mockito.mock(Behavior.class);
		Mockito.doThrow(behaviorException).when(mockedBehavior).run();
		performer.setBehavior(mockedBehavior);

		return performer;
	}
}
