package net.vidageek.crawler.component;

import junit.framework.Assert;
import net.vidageek.crawler.Page;

import org.apache.http.client.HttpClient;
import org.jmock.Mockery;
import org.junit.Test;

/**
 * @author jonasabreu
 * 
 */
final public class WebDownloaderTest {

	@Test
	public void testThatEncodesURLsBeforeCallingGetMethod() {
		Page page = new WebDownloader().get("http://www.google.com/search?q=programação");
		Assert.assertNotNull(page);
		Assert.assertTrue(page.getContent().contains("programação"));
		Assert.assertEquals("http://www.google.com/search?q=programação", page.getUrl());
	}

	@Test
	public void testThatDownloadsURLs() {
		Page page = new WebDownloader().get("http://www.google.com");
		Assert.assertNotNull(page);
		Assert.assertTrue(page.getContent().contains("google"));
	}

	@Test
	public void testThatContentPageWasEncodedCorrect() {
		Mockery mockery = new Mockery();
		HttpClient client = mockery.mock(HttpClient.class);
	}
}
