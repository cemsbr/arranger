package br.usp.ime.arranger;

import java.net.MalformedURLException;

import br.usp.ime.arranger.client.ArrangerProxyCreator;
import br.usp.ime.arranger.service.Arranger;

public class MessageBehavior extends AbstractBehavior {

    private String destWsdl;
    private int requestBytes;
    private int responseBytes;

    private transient StringUtils strUtils = null;
    private transient ArrangerProxyCreator clientCreator = null;
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
            final Arranger destination = clientCreator
                    .getClient(destWsdl);
            final String message = strUtils.getStringOfLength(requestBytes);
            destination.exchangeMessages(message, responseBytes);
        } catch (MalformedURLException e) {
            throw new BehaviorException(e);
        }
    }

    @Override
    public void clear() {
        ArrangerProxyCreator.clearCache();
    }

    private void beReady() {
        if (!readyToRun) {
            strUtils = new StringUtils();
            clientCreator = new ArrangerProxyCreator();
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