package br.usp.ime.arranger.frontend;

import br.usp.ime.arranger.service.MessageBehavior;
import edu.uci.ics.jung.graph.DirectedSparseGraph;

@SuppressWarnings("serial")
public class ArrangerGraphImpl extends
        DirectedSparseGraph<PerformerNode, MessageEdge> implements
        ArrangerGraph {

    @Override
    public boolean addEdge(final MessageEdge edge, final PerformerNode source,
            final PerformerNode dest) {
        final boolean result = super.addEdge(edge, source, dest);
        addMessageBehavior(edge, source, dest);
        return result;
    }

    /**
     * Adds a MessageBehavior to source.
     * 
     * @param edge
     * @param source
     * @param dest
     */
    private void addMessageBehavior(final MessageEdge edge,
            final PerformerNode source, final PerformerNode dest) {
        final MessageBehavior behavior = getMessageBehavior(edge, dest);
        source.addBehavior(behavior);
    }

    /**
     * Translates MessageEdge to MessageBehavior.
     * 
     * @param edge
     * @param dest
     * @return
     */
    private MessageBehavior getMessageBehavior(final MessageEdge edge,
            final PerformerNode dest) {
        final String wsdl = dest.getWsdl();
        final int reqSize = edge.getRequestSize();
        final int resSize = edge.getResponseSize();

        return new MessageBehavior(wsdl, reqSize, resSize);
    }
}
