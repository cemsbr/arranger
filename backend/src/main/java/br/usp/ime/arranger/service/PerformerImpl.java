package br.usp.ime.arranger.service;

import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.jws.WebService;
import javax.xml.ws.soap.MTOM;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.usp.ime.arranger.behaviors.Behavior;
import br.usp.ime.arranger.behaviors.BehaviorException;
import br.usp.ime.arranger.utils.CommUtils;

@MTOM
@WebService(endpointInterface = "br.usp.ime.arranger.service.Performer")
public class PerformerImpl implements Performer {

	private final List<Behavior> behaviors = new ArrayList<>();
	private static final CommUtils COMM = new CommUtils();
	private static final Logger LOG = LogManager.getLogger(PerformerImpl.class
			.getName());

	@Override
	public boolean setBehaviors(final List<Behavior> behaviors)
			throws BehaviorException {
		synchronized (behaviors) {
			clearBehaviors();
			this.behaviors.clear();
			this.behaviors.addAll(behaviors);
		}
		LOG.info("Behaviors set.");
		return true;
	}

	@Override
	public boolean setBehavior(final Behavior behavior)
			throws BehaviorException {
		synchronized (behaviors) {
			clearBehaviors();
			this.behaviors.clear();
			behaviors.add(behavior);
		}
		LOG.info("Behavior set.");
		return true;
	}

	@Override
	public void run() throws BehaviorException {
		for (Behavior behavior : behaviors) {
			behavior.run();
		}
		LOG.debug("Behaviors have just run.");
	}

	@Override
	public List<Behavior> getBehaviors() {
		return behaviors;
	}

	private void clearBehaviors() throws BehaviorException {
		for (Behavior behavior : behaviors) {
			behavior.destroy();
		}
		CommUtils.destroy();
		LOG.debug("Behavior(s) cleared.");
	}

	@Override
	public String msgStringReqStringRes(final String request,
			final int responseBytes) throws BehaviorException {
		LOG.info("String in, String out.");
		run();
		return COMM.getStringMessage(responseBytes);
	}

	@Override
	public String msgDataReqStringRes(final DataHandler request,
			final int responseBytes) throws BehaviorException {
		LOG.info("DataHandler in, String out.");
		COMM.receiveDataHandler(request);
		return msgStringReqStringRes("", responseBytes);
	}

	@Override
	public DataHandler msgStringReqDataRes(final String request,
			final long responseBytes) throws BehaviorException {
		LOG.info("String in, DataHandler out.");
		run();
		return COMM.getDataHandler(responseBytes);
	}

	@Override
	public DataHandler msgDataReqDataRes(DataHandler request, long responseBytes)
			throws BehaviorException {
		LOG.info("DataHanlder in, DataHandler out.");
		COMM.receiveDataHandler(request);
		return msgStringReqDataRes("", responseBytes);
	}
}