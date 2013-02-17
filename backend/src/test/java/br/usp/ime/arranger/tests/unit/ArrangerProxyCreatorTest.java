package br.usp.ime.arranger.tests.unit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.Service;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.usp.ime.arranger.client.ArrangerProxyCreator;

public class ArrangerProxyCreatorTest {

    private transient ArrangerProxyCreator creator;
    private static final ClassLoader CLASSLOADER = Thread.currentThread()
            .getContextClassLoader();
    private transient Map<String, Service> cacheSpy;
    private static String wsdl1, wsdl2;
    private static Field cacheField;

    @BeforeClass
    public static void setUpClass() throws NoSuchFieldException,
            SecurityException {
        wsdl1 = getFileWsdl("arrangerService1.wsdl");
        wsdl2 = getFileWsdl("arrangerService2.wsdl");

        cacheField = ArrangerProxyCreator.class.getDeclaredField("cache");
        cacheField.setAccessible(true);
    }

    @Before
    public void setUp() throws IllegalArgumentException, IllegalAccessException {
        ArrangerProxyCreator.clearCache();
        creator = new ArrangerProxyCreator();
        cacheSpy = spy(new HashMap<String, Service>());
        cacheField.set(null, cacheSpy);
    }

    @Test
    public void shouldAddServiceToCache() throws MalformedURLException {
        verify(cacheSpy, never()).put(anyString(), any(Service.class));
        creator.getClient(wsdl1);
        verify(cacheSpy, times(1)).put(anyString(), any(Service.class));
    }

    @Test
    public void shouldNotReaddServiceToCache() throws MalformedURLException {
        creator.getClient(wsdl1);
        creator.getClient(wsdl1);

        verify(cacheSpy, times(1)).put(anyString(), any(Service.class));
    }

    @Test
    public void shouldAddSecondService() throws MalformedURLException {
        creator.getClient(wsdl1);
        creator.getClient(wsdl2);

        verify(cacheSpy, times(2)).put(anyString(), any(Service.class));
        assertEquals(2, cacheSpy.size());
    }

    @Test
    public void shouldEmptyCacheAfterClear() throws MalformedURLException {
        creator.getClient(wsdl1);
        assertEquals(1, cacheSpy.size());

        ArrangerProxyCreator.clearCache();
        assertEquals(0, cacheSpy.size());
    }

    private static String getFileWsdl(final String filename) {
        final URL url = CLASSLOADER.getResource(filename);
        return "file:" + url.getPath();
    }
}