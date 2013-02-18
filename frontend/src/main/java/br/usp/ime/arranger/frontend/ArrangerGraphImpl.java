package br.usp.ime.arranger.frontend;

import br.usp.ime.arranger.service.MessageBehavior;
import edu.uci.ics.jung.graph.DirectedSparseGraph;

@SuppressWarnings("serial")
public class ArrangerGraphImpl extends
        DirectedSparseGraph<PerformerNode, MessageEdge> implements
        ArrangerGraph {

    @Override
    public boolean addEdge(final MessageEdge msg, final PerformerNode source,
            final PerformerNode dest) {
        final boolean result = super.addEdge(msg, source, dest);
        addMessageBehavior(msg, source, dest);
        return result;
    }

    private void addMessageBehavior(final MessageEdge msgEx,
            final PerformerNode source, final PerformerNode dest) {
        final MessageBehavior behavior = getMessageBehavior(msgEx, dest);
        source.addBehavior(behavior);
    }

    private MessageBehavior getMessageBehavior(final MessageEdge msgEx,
            final PerformerNode dest) {
        final String wsdl = dest.getWsdl();
        final int reqSize = msgEx.getRequestSize();
        final int resSize = msgEx.getResponseSize();

        return new MessageBehavior(wsdl, reqSize, resSize);
    }
}
