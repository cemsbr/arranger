package br.usp.ime.arranger.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import com.sun.xml.ws.developer.JAXWSProperties;

public class PerformerProxyCreator {

	private static final String NAMESPACE = "http://service.arranger.ime.usp.br/";
	private static final String SERVICE_NAME = "PerformerImplService";
	private static final Map<String, Service> CACHE = new HashMap<>();

	public Performer getProxy(final String wsdl) throws MalformedURLException {
		final Service service = getService(wsdl);
		final Performer performer = service.getPort(Performer.class);

		final Map<String, Object> context = ((BindingProvider) performer)
				.getRequestContext();
		// Enable HTTP chunking mode, otherwise HttpURLConnection buffers
		context.put(JAXWSProperties.HTTP_CLIENT_STREAMING_CHUNK_SIZE, 8192);

		return performer;
	}

	public static void destroy() {
		if (!CACHE.isEmpty()) {
			synchronized (CACHE) {
				if (!CACHE.isEmpty()) {
					CACHE.clear();
				}
			}
		}
	}

	private Service getService(final String wsdl) throws MalformedURLException {
		if (!CACHE.containsKey(wsdl)) {
			synchronized (CACHE) {
				if (!CACHE.containsKey(wsdl)) {
					createService(wsdl);
				}
			}
		}
		return CACHE.get(wsdl);
	}

	private void createService(final String wsdl) throws MalformedURLException {
		final QName qname = new QName(NAMESPACE, SERVICE_NAME);
		final URL url = new URL(wsdl);
		final Service service = Service.create(url, qname);

		CACHE.put(wsdl, service);
	}
}