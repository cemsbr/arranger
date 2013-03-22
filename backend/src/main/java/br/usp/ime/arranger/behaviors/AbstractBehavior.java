package br.usp.ime.arranger.behaviors;

public abstract class AbstractBehavior implements Behavior {
    
    @Override
    public void destroy() throws BehaviorException { // NOPMD
        // It is run before setting new Web Service behaviors
    }
}