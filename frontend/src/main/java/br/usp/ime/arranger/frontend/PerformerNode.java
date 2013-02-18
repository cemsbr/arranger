package br.usp.ime.arranger.frontend;

import java.util.ArrayList;
import java.util.List;

import br.usp.ime.arranger.behaviors.Behavior;

public class PerformerNode {

    private String wsdl;
    private List<Behavior> behaviors;

    private String label;

    public PerformerNode(final String wsdl) {
        this.wsdl = wsdl;
        behaviors = new ArrayList<>();
        label = "";
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

    public void addBehavior(final Behavior behavior) {
        behaviors.add(behavior);
    }

    public void addBehaviors(final List<Behavior> behaviors) {
        this.behaviors.addAll(behaviors);
    }

    @Override
    public String toString() {
        return label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}