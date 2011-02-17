package net.vidageek.crawler.component;

import net.vidageek.crawler.Page;

import org.junit.Assert;
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

}
