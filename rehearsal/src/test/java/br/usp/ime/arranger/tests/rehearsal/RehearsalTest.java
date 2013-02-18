package br.usp.ime.arranger.tests.rehearsal;

import java.io.IOException;

import org.apache.xmlbeans.XmlException;

import eu.choreos.vv.exceptions.MockDeploymentException;
import eu.choreos.vv.exceptions.WSDLException;
import eu.choreos.vv.interceptor.MessageInterceptor;

public final class RehearsalTest {

    private static final String PORT = "8082";
    private static MessageInterceptor interceptor;
    
    private RehearsalTest() {
    }
    
    protected static MessageInterceptor intercept(final String realWsdl)
            throws WSDLException, MockDeploymentException, XmlException,
            IOException {
        interceptor = new MessageInterceptor(PORT);
        interceptor.interceptMessagesTo(realWsdl);
        return interceptor;
    }
}