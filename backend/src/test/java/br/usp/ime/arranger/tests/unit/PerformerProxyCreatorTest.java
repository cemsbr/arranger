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

import br.usp.ime.arranger.service.PerformerProxyCreator;
import br.usp.ime.tests.utils.ReflectionUtils;

public class PerformerProxyCreatorTest {

	private transient PerformerProxyCreator creator;
	private static final ClassLoader CLASSLOADER = Thread.currentThread()
			.getContextClassLoader();
	private transient Map<String, Service> cacheSpy;
	private static String wsdl1, wsdl2;
	private static Field cacheField;

	@BeforeClass
	public static void setUpClass() throws NoSuchFieldException,
			SecurityException, IllegalArgumentException, IllegalAccessException {
		wsdl1 = getFileWsdl("performer0.wsdl");
		wsdl2 = getFileWsdl("performer1.wsdl");

		cacheField = ReflectionUtils.setFieldPublic(
				PerformerProxyCreator.class, "CACHE");
	}

	@Before
	public void setUp() throws IllegalArgumentException, IllegalAccessException {
		PerformerProxyCreator.destroy();
		creator = new PerformerProxyCreator();
		cacheSpy = spy(new HashMap<String, Service>());
		cacheField.set(null, cacheSpy);
	}

	@Test
	public void shouldAddServiceToCache() throws MalformedURLException {
		verify(cacheSpy, never()).put(anyString(), any(Service.class));
		creator.getProxy(wsdl1);
		verify(cacheSpy, times(1)).put(anyString(), any(Service.class));
	}

	@Test
	public void shouldNotReaddServiceToCache() throws MalformedURLException {
		creator.getProxy(wsdl1);
		creator.getProxy(wsdl1);

		verify(cacheSpy, times(1)).put(anyString(), any(Service.class));
	}

	@Test
	public void shouldAddSecondService() throws MalformedURLException {
		creator.getProxy(wsdl1);
		creator.getProxy(wsdl2);

		verify(cacheSpy, times(2)).put(anyString(), any(Service.class));
		assertEquals(2, cacheSpy.size());
	}

	@Test
	public void shouldEmptyCacheAfterClear() throws MalformedURLException {
		creator.getProxy(wsdl1);
		assertEquals(1, cacheSpy.size());

		PerformerProxyCreator.destroy();
		assertEquals(0, cacheSpy.size());
	}

	private static String getFileWsdl(final String filename) {
		final URL url = CLASSLOADER.getResource(filename);
		return "file:" + url.getPath();
	}
}