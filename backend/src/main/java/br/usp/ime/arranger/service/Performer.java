package br.usp.ime.arranger.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

import br.usp.ime.arranger.behaviors.Behavior;
import br.usp.ime.arranger.behaviors.BehaviorException;
import br.usp.ime.arranger.behaviors.FibonacciBehavior;
import br.usp.ime.arranger.behaviors.SleepBehavior;

@WebService
@SOAPBinding
@XmlSeeAlso({ FibonacciBehavior.class, MessageBehavior.class,
        SleepBehavior.class })
public interface Performer {

    @WebMethod
    boolean setBehaviors(final List<Behavior> behaviors);

    @WebMethod
    boolean setBehavior(final Behavior behavior);

    @WebMethod
    String exchangeMessages(final String request, final int responseBytes)
            throws BehaviorException;

    @WebMethod
    void run() throws BehaviorException;

    @WebMethod
    List<Behavior> getBehaviors();
}