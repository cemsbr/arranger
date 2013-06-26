package br.usp.ime.arranger;

import java.io.IOException;

import org.apache.log4j.Logger;

@SuppressWarnings("PMD.MoreThanOneLogger")
public abstract class AbstractLoadGenerator {

	protected static final int THREADS_TIMEOUT = 360;
	protected static final Logger GRAPH = Logger.getLogger("graphsLogger");
	protected static final Logger CONSOLE = Logger
			.getLogger(AbstractLoadGenerator.class);

	public abstract void generateLoad(final String args[], int start)
			throws IOException;
}