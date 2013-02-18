package br.usp.ime.arranger.service;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import br.usp.ime.arranger.utils.StringUtils;
import br.usp.ime.arranger.behaviors.Behavior;
import br.usp.ime.arranger.behaviors.BehaviorException;

@WebService(endpointInterface = "br.usp.ime.arranger.service.Performer")
public class PerformerImpl implements Performer {

    private final List<Behavior> behaviors = new ArrayList<>();

    @Override
    public boolean setBehaviors(final List<Behavior> behaviors) {
        synchronized (behaviors) {
            clearBehaviors();
            this.behaviors.clear();
            this.behaviors.addAll(behaviors);
        }
        return true;
    }

    @Override
    public boolean setBehavior(final Behavior behavior) {
        synchronized (behaviors) {
            clearBehaviors();
            this.behaviors.clear();
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
            behavior.destroy();
        }
    }
}