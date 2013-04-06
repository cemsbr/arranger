package br.usp.ime.arranger.tests.functional;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Endpoint;

import br.usp.ime.arranger.service.PerformerImpl;

public class PerformerPublisher {

    private static final int PORT = 8081;
    private List<Endpoint> endpoints = new ArrayList<>();
    private List<String> wsdls = new ArrayList<>();

    public static void main(final String[] args) {
        final PerformerPublisher servers = new PerformerPublisher();
        servers.publish(2);
    }

    /**
     * Publish endpoints and return their wsdls.
     * 
     * @param amount
     *            of endpoints.
     * @return a list of wsdls.
     */
    public List<String> publish(final int amount) {
        Endpoint endpoint;
        String address;

        for (int i = 1; i <= amount; i++) {
            address = getAddress(i);
            endpoint = Endpoint.publish(address, new PerformerImpl());
            endpoints.add(endpoint);
            wsdls.add(address + "?wsdl");
        }

        return wsdls;
    }

    public void stopAll() {
        for (Endpoint endpoint : endpoints) {
            endpoint.stop();
        }
        wsdls.clear();
    }

    private String getAddress(final int instance) {
        return "http://127.0.0.1:" + PORT + "/performer" + instance
                + "/endpoint";
    }
}