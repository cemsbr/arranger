package br.usp.ime.arranger.frontend;

import java.util.ArrayList;
import java.util.List;

import br.usp.ime.arranger.Behavior;

public class WebService {

    private String wsdl;
    private List<Behavior> behaviors;

    public WebService(final String wsdl) {
        this.wsdl = wsdl;
    }

    public String getWsdl() {
        return wsdl;
    }

    public void setWsdl(final String wsdl) {
        this.wsdl = wsdl;
    }

    public List<Behavior> getBehaviors() {
        return behaviors;
    }

    public void setBehaviors(final List<Behavior> behaviors) {
        this.behaviors = behaviors;
    }

    public void setBehavior(final Behavior behavior) {
        this.behaviors = new ArrayList<>();
        this.behaviors.add(behavior);
    }

    public void addBehavior(final Behavior behavior) {
        if (behaviors == null) {
            setBehavior(behavior);
        } else {
            behaviors.add(behavior);
        }
    }

    public void addBehaviors(final List<Behavior> behaviors) {
        if (this.behaviors == null) {
            setBehaviors(behaviors);
        } else {
            behaviors.addAll(behaviors);
        }
    }
}