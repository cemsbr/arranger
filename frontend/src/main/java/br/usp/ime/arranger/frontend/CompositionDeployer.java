package br.usp.ime.arranger.frontend;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;

import br.usp.ime.arranger.Behavior;
import br.usp.ime.arranger.client.ArrangerProxyCreator;
import br.usp.ime.arranger.service.Arranger;
import edu.uci.ics.jung.graph.DirectedGraph;

public class CompositionDeployer {
    
    private transient final ArrangerProxyCreator clientCreator = new ArrangerProxyCreator();

    public void deploy(final DirectedGraph<WebService, MessageExchange> graph)
            throws MalformedURLException {
        final Collection<WebService> webServices = graph.getVertices();
        for (WebService webService : webServices) {
            setBehavior(webService);
        }
    }

    private void setBehavior(final WebService webService) throws MalformedURLException {
        final List<Behavior> behaviors = webService.getBehaviors();
        final Arranger realWs = clientCreator.getClient(webService.getWsdl());
        realWs.setBehaviors(behaviors);
    }
}