package br.usp.ime.arranger.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;


public class PerformerProxyCreator {

    private static final String NAMESPACE = "http://service.arranger.ime.usp.br/";
    private static final String SERVICE_NAME = "PerformerImplService";
    private static Map<String, Service> cache = null;

    public Performer getProxy(final String wsdl)
            throws MalformedURLException {
        checkCache(wsdl);
        final Service service = cache.get(wsdl);
        return service.getPort(Performer.class);
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
            synchronized (PerformerProxyCreator.class) {
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