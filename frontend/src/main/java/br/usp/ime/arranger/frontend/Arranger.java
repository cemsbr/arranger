package br.usp.ime.arranger.frontend;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;

import br.usp.ime.arranger.behaviors.Behavior;
import br.usp.ime.arranger.service.Performer;
import br.usp.ime.arranger.service.PerformerProxyCreator;

public class Arranger {

    private transient final PerformerProxyCreator clientCreator = new PerformerProxyCreator();

    public static final int MB = 1024 * 1024; // NOPMD

    public void deploy(final ArrangerGraph graph) throws MalformedURLException {
        final Collection<PerformerNode> webServices = graph.getVertices();
        for (PerformerNode webService : webServices) {
            setBehavior(webService);
        }
    }

    private void setBehavior(final PerformerNode webService)
            throws MalformedURLException {
        final List<Behavior> behaviors = webService.getBehaviors();
        final Performer realWs = clientCreator.getProxy(webService.getWsdl());
        realWs.setBehaviors(behaviors);
    }
}