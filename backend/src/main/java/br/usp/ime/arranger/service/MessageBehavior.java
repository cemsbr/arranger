package br.usp.ime.arranger.service;

import java.net.MalformedURLException;

import javax.activation.DataHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.usp.ime.arranger.behaviors.AbstractBehavior;
import br.usp.ime.arranger.behaviors.BehaviorException;
import br.usp.ime.arranger.utils.CommUtils;

public class MessageBehavior extends AbstractBehavior {

	private String destWsdl;
	private long requestBytes;
	private long responseBytes;
	private static final CommUtils COMM = new CommUtils();
	private static final Logger LOG = LogManager
			.getLogger(MessageBehavior.class.getName());

	// MTOM will be used for messages of size >= MTOM_THRESHOLD bytes to reduce
	// memory consumption.
	private static int mtomMinSize = 1024 * 1024;

	public MessageBehavior(final String destWsdl, final long requestBytes,
			final long responseBytes) {
		super();
		this.destWsdl = destWsdl;
		this.requestBytes = requestBytes;
		this.responseBytes = responseBytes;
	}

	@Override
	public void run() throws BehaviorException {
		try {
			talkToPerformer();
		} catch (MalformedURLException e) {
			throw new BehaviorException("MalformedURL: " + destWsdl + ".", e);
		}
	}

	private void talkToPerformer() throws MalformedURLException,
			BehaviorException {
		final Performer performer = COMM.getPerformer(destWsdl);
		final boolean requestIsBig = (requestBytes >= mtomMinSize);
		final boolean responseIsBig = (responseBytes >= mtomMinSize);

		if (requestIsBig) {
			makeDataReq(responseIsBig, performer);
		} else {
			makeStringReq(responseIsBig, performer);
		}
	}

	private void makeDataReq(final boolean responseIsBig,
			final Performer performer) throws BehaviorException {
		LOG.info("Request = DataHandler.");
		final DataHandler dhOut = COMM.getDataHandler(requestBytes);
		if (responseIsBig) {
			LOG.info("Response = DataHandler");
			final DataHandler dhIn = performer.msgDataReqDataRes(dhOut,
					responseBytes);
			LOG.debug("Received DataHandler. Writing to file...");
			COMM.receiveDataHandler(dhIn);
		} else {
			LOG.info("Response = String");
			performer.msgDataReqStringRes(dhOut, (int) responseBytes);
		}
		LOG.debug("Finished DataHandler request.");
	}

	private void makeStringReq(final boolean responseIsBig,
			final Performer performer) throws BehaviorException {
		LOG.info("Request = String.");
		final String msg = COMM.getStringMessage((int) requestBytes);
		if (responseIsBig) {
			LOG.info("Response = DataHandler.");
			final DataHandler dhIn = performer.msgStringReqDataRes(msg,
					responseBytes);
			LOG.debug("Received DataHandler. Writing to file...");
			COMM.receiveDataHandler(dhIn);
		} else {
			LOG.info("Response = String.");
			performer.msgStringReqStringRes(msg, (int) responseBytes);
		}
		LOG.debug("Finished DataHandler request.");
	}

	public static int getMtomMinSize() {
		return mtomMinSize;
	}

	public static void setMtomMinSize(final int bytes) {
		mtomMinSize = bytes;
	}

	// Needed for serialization
	public MessageBehavior() {
		super();
	}

	public String getDestWsdl() {
		return destWsdl;
	}

	public void setDestWsdl(final String destWsdl) {
		this.destWsdl = destWsdl;
	}

	public long getRequestBytes() {
		return requestBytes;
	}

	public void setRequestBytes(final long bytes) {
		this.requestBytes = bytes;
	}

	public long getResponseBytes() {
		return responseBytes;
	}

	public void setResponseBytes(final long bytes) {
		this.responseBytes = bytes;
	}
}