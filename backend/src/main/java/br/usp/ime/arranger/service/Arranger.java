package br.usp.ime.arranger.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

import br.usp.ime.arranger.Behavior;
import br.usp.ime.arranger.BehaviorException;
import br.usp.ime.arranger.CpuBehavior;
import br.usp.ime.arranger.MessageBehavior;
import br.usp.ime.arranger.SleepBehavior;

@WebService
@SOAPBinding
@XmlSeeAlso({ CpuBehavior.class, MessageBehavior.class, SleepBehavior.class })
public interface Arranger {

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