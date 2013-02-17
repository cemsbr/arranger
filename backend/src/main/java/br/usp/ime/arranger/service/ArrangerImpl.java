package br.usp.ime.arranger.service;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import br.usp.ime.arranger.Behavior;
import br.usp.ime.arranger.BehaviorException;
import br.usp.ime.arranger.StringUtils;

@WebService(endpointInterface = "br.usp.ime.arranger.service.Arranger")
public class ArrangerImpl implements Arranger {

    private final List<Behavior> behaviors = new ArrayList<>();

    @Override
    public boolean setBehaviors(final List<Behavior> behaviors) {
        synchronized (behaviors) {
            clearBehaviors();
            this.behaviors.addAll(behaviors);
        }
        return true;
    }

    @Override
    public boolean setBehavior(final Behavior behavior) {
        synchronized (behaviors) {
            clearBehaviors();
            behaviors.add(behavior);
        }
        return true;
    }

    @Override
    public String exchangeMessages(final String request, final int responseBytes)
            throws BehaviorException {
        run();
        final StringUtils strUtils = new StringUtils();
        return strUtils.getStringOfLength(responseBytes);
    }

    @Override
    public void run() throws BehaviorException {
        for (Behavior behavior : behaviors) {
            behavior.run();
        }
    }

    @Override
    public List<Behavior> getBehaviors() {
        return behaviors;
    }

    private void clearBehaviors() {
        for (Behavior behavior : behaviors) {
            behavior.clear();
        }
    }
}