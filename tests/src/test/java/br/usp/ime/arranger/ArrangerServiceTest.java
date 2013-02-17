package br.usp.ime.arranger;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import br.usp.ime.arranger.client.ArrangerProxyCreator;
import br.usp.ime.arranger.service.Arranger;
import eu.choreos.vv.clientgenerator.Item;
import eu.choreos.vv.exceptions.MockDeploymentException;
import eu.choreos.vv.exceptions.WSDLException;
import eu.choreos.vv.interceptor.MessageInterceptor;

public class ArrangerServiceTest {

    private static Server servers;
    private static Arranger ws1;
    private static MessageInterceptor interceptor;

    private transient List<Item> messages;

    @BeforeClass
    public static void setUpClass() throws WSDLException,
            MockDeploymentException, XmlException, IOException {
        servers = new Server();
        final List<String> wsdls = servers.start(2);
        final ArrangerProxyCreator clientCreator = new ArrangerProxyCreator();
        ws1 = clientCreator.getClient(wsdls.get(0));
        interceptor = RehearsalTest.intercept(wsdls.get(1));
    }

    @AfterClass
    public static void tearDownClass() {
        interceptor.stop();
        servers.stopAll();
    }

    @Test
    public void shouldSendMessage() throws BehaviorException,
            NoSuchFieldException {
        final Behavior msg = new MessageBehavior(interceptor.getProxyWsdl(),
                64, 256);
        ws1.setBehavior(msg);
        ws1.run();

        messages = interceptor.getMessages();
        assertEquals(1, messages.size());
        final StringUtils strUtils = new StringUtils();
        assertEquals(strUtils.getStringOfLength(64), messages.get(0)
                .getContent("arg0"));
    }

    @Test
    public void shouldNotSendMessage() throws BehaviorException {
        final Behavior cpu = new CpuBehavior(100, 100);
        ws1.setBehavior(cpu);
        ws1.run();

        messages = interceptor.getMessages();
        assertEquals(0, messages.size());
    }
}