package br.usp.ime.arranger.frontend;

import java.net.MalformedURLException;
import java.util.List;

import br.usp.ime.arranger.Behavior;
import br.usp.ime.arranger.CpuBehavior;
import br.usp.ime.arranger.SleepBehavior;

public final class DataCentricOrch {

    private DataCentricOrch() {
    }

    public static void main(final String[] args) throws MalformedURLException {
        final Frontend hall = new Frontend();

        // TODO: Need to be dynamic
        final List<String> wsdls = hall.getMusicians();

        final WebService ws1 = new WebService(wsdls.get(0));
        final WebService ws2 = new WebService(wsdls.get(1));
        final WebService ws3 = new WebService(wsdls.get(2));
        final WebService ws4 = new WebService(wsdls.get(3));
        final WebService ws5 = new WebService(wsdls.get(4));
        final WebService ws6 = new WebService(wsdls.get(5));
        final WebService ws7 = new WebService(wsdls.get(6));

        // Graph by Jung2 - http://jung.sourceforge.net/
        final ArrangerGraph graph = new ArrangerGraphImpl();
        graph.addVertex(ws1);
        graph.addVertex(ws2);
        graph.addVertex(ws3);
        graph.addVertex(ws4);
        graph.addVertex(ws5);
        graph.addVertex(ws6);
        graph.addVertex(ws7);

        /*
         * Orchestrator (ws4) behavior: First, it gathers data from ws1, 2 and 3
         */
        graph.addEdge(new MessageExchange(16, 53 * Frontend.MB), ws4, ws1);
        graph.addEdge(new MessageExchange(16, 53 * Frontend.MB), ws4, ws2);
        graph.addEdge(new MessageExchange(16, 53 * Frontend.MB), ws4, ws3);

        // Data providers (external services) behavior
        final Behavior sleep = new SleepBehavior(250);
        ws1.addBehavior(sleep);
        ws2.addBehavior(sleep);
        ws3.addBehavior(sleep);

        // Then, orchestrator prepares the data for processing nodes
        final Behavior cpuLow = new CpuBehavior(300, 50);
        ws4.addBehavior(cpuLow);

        // Finally, it sends the data for each processing node;
        graph.addEdge(new MessageExchange(53 * Frontend.MB, 7 * Frontend.MB),
                ws4, ws5);
        graph.addEdge(new MessageExchange(53 * Frontend.MB, 7 * Frontend.MB),
                ws4, ws6);
        graph.addEdge(new MessageExchange(53 * Frontend.MB, 7 * Frontend.MB),
                ws4, ws7);

        // Processing nodes behavior
        final Behavior cpuHigh = new CpuBehavior(500, 100);
        ws5.addBehavior(cpuHigh);
        ws6.addBehavior(cpuHigh);
        ws7.addBehavior(cpuHigh);

        final CompositionDeployer deployer = new CompositionDeployer();
        deployer.deploy(graph);

        hall.display(graph);
    }
}