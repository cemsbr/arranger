package br.usp.ime.arranger.behaviors;

public class BehaviorException extends Exception {

    private static final long serialVersionUID = 4141225804033769065L;

    public BehaviorException(final Throwable cause) {
        super(cause);
    }
    
    public BehaviorException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}