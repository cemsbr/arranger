package br.usp.ime.arranger.frontend;

public class MessageEdge {

	private int requestSize;
	private int responseSize;

	public MessageEdge(final int requestSize, final int responseSize) {
		this.requestSize = requestSize;
		this.responseSize = responseSize;
	}

	public int getRequestSize() {
		return requestSize;
	}

	public void setRequestSize(final int requestSize) {
		this.requestSize = requestSize;
	}

	public int getResponseSize() {
		return responseSize;
	}

	public void setResponseSize(final int responseSize) {
		this.responseSize = responseSize;
	}
}