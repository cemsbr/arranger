package br.usp.ime.arranger;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Endpoint;

import br.usp.ime.arranger.service.ArrangerImpl;

public class Server {

    private static final int PORT = 8081;
    private List<Endpoint> endpoints = new ArrayList<>();
    private List<String> wsdls = new ArrayList<>();

    public static void main(final String[] args) {
        final Server servers = new Server();
        servers.start(2);
    }

    public List<String> start(final int amount) {
        Endpoint endpoint;
        String address;
        for (int i = 1; i <= amount; i++) {
            address = getAddress(i);
            endpoint = Endpoint.publish(address, new ArrangerImpl());
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
        return "http://localhost:" + PORT + "/arranger" + instance
                + "/endpoint";
    }
}