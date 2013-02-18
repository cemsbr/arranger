package br.usp.ime.arranger.frontend;

import br.usp.ime.arranger.MessageBehavior;
import edu.uci.ics.jung.graph.DirectedSparseGraph;

@SuppressWarnings("serial")
public class ArrangerGraphImpl extends
        DirectedSparseGraph<WebService, MessageExchange> implements ArrangerGraph {

    @Override
    public boolean addEdge(final MessageExchange msg, final WebService source,
            final WebService dest) {
        final boolean result = super.addEdge(msg, source, dest);
        addMessageBehavior(msg, source, dest);
        return result;
    }

    private void addMessageBehavior(final MessageExchange msgEx,
            final WebService source, final WebService dest) {
        final MessageBehavior behavior = getMessageBehavior(msgEx, dest);
        source.addBehavior(behavior);
    }

    private MessageBehavior getMessageBehavior(final MessageExchange msgEx,
            final WebService dest) {
        final String wsdl = dest.getWsdl();
        final int reqSize = msgEx.getRequestSize();
        final int resSize = msgEx.getResponseSize();

        return new MessageBehavior(wsdl, reqSize, resSize);
    }
}