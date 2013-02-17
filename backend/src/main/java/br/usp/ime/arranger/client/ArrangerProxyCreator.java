package br.usp.ime.arranger.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import br.usp.ime.arranger.service.Arranger;

public class ArrangerProxyCreator {

    private static final String NAMESPACE = "http://service.arranger.ime.usp.br/";
    private static final String SERVICE_NAME = "ArrangerServiceImplService";
    private static Map<String, Service> cache = null;

    public Arranger getClient(final String wsdl)
            throws MalformedURLException {
        checkCache(wsdl);
        final Service service = cache.get(wsdl);
        return service.getPort(Arranger.class);
    }

    public static void clearCache() {
        if (cache != null) {
            synchronized (cache) {
                if (cache != null) {
                    cache.clear();
                }
            }
        }
    }

    private void checkCache(final String wsdl) throws MalformedURLException {
        if (cache == null) {
            synchronized (ArrangerProxyCreator.class) {
                if (cache == null) {
                    cache = new HashMap<>();
                    createService(wsdl);
                }
            }
        } else if (!cache.containsKey(wsdl)) {
            synchronized (cache) {
                if (!cache.containsKey(wsdl)) {
                    createService(wsdl);
                }
            }
        }
    }

    private void createService(final String wsdl) throws MalformedURLException {
        final QName qname = new QName(NAMESPACE, SERVICE_NAME);
        final URL url = new URL(wsdl);
        final Service service = Service.create(url, qname);
        
        cache.put(wsdl, service);
    }
}