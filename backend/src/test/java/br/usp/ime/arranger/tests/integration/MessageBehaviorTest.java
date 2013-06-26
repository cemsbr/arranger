package br.usp.ime.arranger.tests.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.activation.DataHandler;

import org.junit.BeforeClass;
import org.junit.Test;

import br.usp.ime.arranger.service.MessageBehavior;
import br.usp.ime.arranger.service.Performer;
import br.usp.ime.arranger.service.PerformerImpl;
import br.usp.ime.arranger.utils.CommUtils;
import br.usp.ime.tests.utils.ReflectionUtils;

@SuppressWarnings("PMD.SignatureDeclareThrowsException")
public class MessageBehaviorTest {

	private static int mtomMinSize;
	private transient Performer performer;

	@BeforeClass
	public static void oneTimeSetUp() throws NoSuchFieldException,
			SecurityException, IllegalArgumentException, IllegalAccessException {
		mtomMinSize = MessageBehavior.getMtomMinSize();
		ReflectionUtils.setFieldPublic(MessageBehavior.class, "COMM");
	}

	@Test
	public void testBigRequestBigResponse() throws Exception {
		performer = mockMessage();
		new MessageBehavior("", mtomMinSize, mtomMinSize).run();

		verify(performer, times(1)).msgDataReqDataRes(any(DataHandler.class),
				anyLong());
	}

	@Test
	public void testSmallRequestSmallResponse() throws Exception {
		final int size = mtomMinSize - 1;
		performer = mockMessage();
		new MessageBehavior("", size, size).run();

		verify(performer, times(1))
				.msgStringReqStringRes(anyString(), anyInt());
	}

	@Test
	public void testBigRequestSmallResponse() throws Exception {
		performer = mockMessage();
		new MessageBehavior("", mtomMinSize, mtomMinSize - 1).run();

		verify(performer, times(1)).msgDataReqStringRes(any(DataHandler.class),
				anyInt());
	}

	@Test
	public void testSmallRequestBigResponse() throws Exception {
		performer = mockMessage();
		new MessageBehavior("", mtomMinSize - 1, mtomMinSize).run();

		verify(performer, times(1)).msgStringReqDataRes(anyString(), anyLong());
	}

	@Test
	public void testMalformedUrl() throws Exception {
		ReflectionUtils
				.setField(MessageBehavior.class, "COMM", new CommUtils());
		final String url = "****";
		final String expectedMsg = "MalformedURL: " + url + ".";
		final MessageBehavior msg = new MessageBehavior(url, 42, 42);
		try {
			msg.run();
			fail();
		} catch (Exception e) {
			assertEquals(expectedMsg, e.getMessage());
		}
	}

	private Performer mockMessage() throws Exception {
		performer = mock(PerformerImpl.class);
		final CommUtils comm = mock(CommUtils.class);
		when(comm.getPerformer(anyString())).thenReturn(performer);

		ReflectionUtils.setField(MessageBehavior.class, "COMM", comm);

		return performer;
	}
}