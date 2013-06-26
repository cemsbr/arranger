package br.usp.ime.arranger.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.usp.ime.arranger.behaviors.BehaviorException;
import br.usp.ime.arranger.service.Performer;
import br.usp.ime.arranger.service.PerformerProxyCreator;

import com.sun.xml.ws.developer.StreamingDataHandler;

/**
 * @author cadu Thread-safe.
 */
public class CommUtils {

	private static final Logger LOG = LogManager.getLogger(CommUtils.class
			.getName());

	public Performer getPerformer(final String wsdl)
			throws MalformedURLException {
		final PerformerProxyCreator proxyCreator = new PerformerProxyCreator();
		return proxyCreator.getProxy(wsdl);
	}

	public DataHandler getDataHandler(final long sizeInBytes)
			throws BehaviorException {
		final FileCreator creator = new FileCreator();
		DataHandler dataHandler = null;

		try {
			final File reqFile = creator.getFileOfSize(sizeInBytes);
			LOG.debug("Using file " + reqFile.getAbsolutePath() + ".");
			final DataSource dataSource = new FileDataSource(reqFile);
			dataHandler = new DataHandler(dataSource);
		} catch (IOException e) {
			throw new BehaviorException("Exception while creating file.", e);
		}

		return dataHandler;
	}

	public String getStringMessage(final int sizeInBytes) {
		final StringUtils strUtil = new StringUtils();
		return strUtil.getStringOfLength(sizeInBytes);
	}

	public void receiveDataHandler(final DataHandler dataHandler)
			throws BehaviorException {
		final StreamingDataHandler stream = (StreamingDataHandler) dataHandler;
		try {
			final File file = FileCreator.createTempFile();
			LOG.debug("Created file " + file.getAbsolutePath());
			stream.moveTo(file);
			stream.close();
			file.delete();
		} catch (IOException e) {
			throw new BehaviorException("Couldn't save data handler to file.",
					e);
		}
	}

	public static void destroy() {
		PerformerProxyCreator.destroy();
		FileCreator.destroy();
	}
}