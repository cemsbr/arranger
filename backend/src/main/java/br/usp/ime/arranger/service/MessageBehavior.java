package br.usp.ime.arranger.service;

import java.net.MalformedURLException;

import br.usp.ime.arranger.behaviors.AbstractBehavior;
import br.usp.ime.arranger.behaviors.BehaviorException;
import br.usp.ime.arranger.utils.StringUtils;

public class MessageBehavior extends AbstractBehavior {

    private String destWsdl;
    private int requestBytes;
    private int responseBytes;

    private transient StringUtils strUtils = null;
    private transient PerformerProxyCreator clientCreator = null;
    private transient boolean readyToRun = false;

    public MessageBehavior(final String destWsdl, final int requestBytes,
            final int responseBytes) {
        super();
        this.destWsdl = destWsdl;
        this.requestBytes = requestBytes;
        this.responseBytes = responseBytes;
    }

    @Override
    public void run() throws BehaviorException {
        beReady();
        try {
            final Performer destination = clientCreator.getProxy(destWsdl);
            final String message = strUtils.getStringOfLength(requestBytes);
            destination.exchangeMessages(message, responseBytes);
        } catch (MalformedURLException e) {
            throw new BehaviorException(e);
        }
    }

    @Override
    public void destroy() {
        PerformerProxyCreator.clearCache();
    }

    private void beReady() {
        if (!readyToRun) {
            strUtils = new StringUtils();
            clientCreator = new PerformerProxyCreator();
            readyToRun = true;
        }
    }

    // Needed for serialization
    public MessageBehavior() {
        super();
    }

    public String getDestWsdl() {
        return destWsdl;
    }

    public void setDestWsdl(final String destWsdl) {
        this.destWsdl = destWsdl;
    }

    public int getRequestBytes() {
        return requestBytes;
    }

    public void setRequestBytes(final int bytes) {
        this.requestBytes = bytes;
    }

    public int getResponseBytes() {
        return responseBytes;
    }

    public void setResponseBytes(final int bytes) {
        this.responseBytes = bytes;
    }
}