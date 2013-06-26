package br.usp.ime.arranger;

import java.net.MalformedURLException;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import br.usp.ime.arranger.behaviors.BehaviorException;
import br.usp.ime.arranger.service.Performer;
import br.usp.ime.arranger.service.PerformerProxyCreator;

@SuppressWarnings("PMD.MoreThanOneLogger")
public abstract class AbstractClient {

	protected static final Logger GRAPH = Logger.getLogger("graphsLogger");
	private static final Logger CONSOLE = Logger
			.getLogger(AbstractClient.class);

	protected static CountDownLatch latch;
	protected final int threadNumber;
	protected final Performer server;

	private static final PerformerProxyCreator PROXY_CREATOR = new PerformerProxyCreator();
	private static String wsdl;

	protected abstract void simulate() throws BehaviorException;

	public AbstractClient(final int threadNumber) throws MalformedURLException {
		CONSOLE.debug("Thread " + threadNumber + " has started.");
		this.threadNumber = threadNumber;
		server = PROXY_CREATOR.getProxy(wsdl);
	}

	public static void setWsdl(final String wsdl) {
		AbstractClient.wsdl = wsdl;
	}

	public static void setCountDownLatch(final CountDownLatch latch) {
		AbstractClient.latch = latch;
	}

	protected void logError(final String message) {
		GRAPH.error("# ERROR " + message);
		CONSOLE.error(message);
	}

	protected void logError(final String message, final Exception exception) {
		GRAPH.error("# ERROR " + message);
		CONSOLE.error(message, exception);
	}
}