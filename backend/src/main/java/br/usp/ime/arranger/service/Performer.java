package br.usp.ime.arranger.service;

import java.util.List;

import javax.activation.DataHandler;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

import br.usp.ime.arranger.behaviors.Behavior;
import br.usp.ime.arranger.behaviors.BehaviorException;
import br.usp.ime.arranger.behaviors.FibonacciBehavior;
import br.usp.ime.arranger.behaviors.MemoryBehavior;
import br.usp.ime.arranger.behaviors.SleepBehavior;

@WebService
@SOAPBinding
@XmlSeeAlso({ FibonacciBehavior.class, MemoryBehavior.class,
        MessageBehavior.class, SleepBehavior.class })
public interface Performer {

    @WebMethod
    boolean setBehaviors(final List<Behavior> behaviors)
            throws BehaviorException;

    @WebMethod
    boolean setBehavior(final Behavior behavior) throws BehaviorException;

    @WebMethod
    String msgStringReqStringRes(final String request, final int responseBytes)
            throws BehaviorException;

    @WebMethod
    String msgDataReqStringRes(final DataHandler request,
            final int responseBytes) throws BehaviorException;

    @WebMethod
    DataHandler msgStringReqDataRes(final String request,
            final long responseBytes) throws BehaviorException;

    @WebMethod
    DataHandler msgDataReqDataRes(final DataHandler request,
            final long responseBytes) throws BehaviorException;

    @WebMethod
    void run() throws BehaviorException;

    @WebMethod
    List<Behavior> getBehaviors();
}