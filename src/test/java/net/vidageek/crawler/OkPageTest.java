/**
 * 
 */
package net.vidageek.crawler;

import static org.junit.Assert.assertEquals;

import java.util.List;

import net.vidageek.crawler.page.OkPage;

import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;

/**
 * @author jonasabreu
 * 
 */

@RunWith(Theories.class)
public class OkPageTest {

	@Test(expected = IllegalArgumentException.class)
	public void testThatThrowsExceptionIfUrlIsNull() {
		new OkPage(null, "content");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThatThrowsExceptionIfUrlIsEmpty() {
		new OkPage("  \n   \t   ", "content");
	}

	@Test
	public void testThatCanRecoverTwoLinks() {

		List<String> links = new OkPage("defaultUrl", "<a href=\"test link\">\n\n <a \nhref=\"test link2\">")
				.getLinks();

		assertEquals(2, links.size());
		assertEquals("test link", links.get(0));
		assertEquals("test link2", links.get(1));
	}

	@Test
	public void testThatCanRecoverIframeLink() {
		List<String> links = new OkPage("defaultUrl", "<iframe href=\"test.page\">").getLinks();

		assertEquals(1, links.size());
		assertEquals("test.page", links.get(0));
	}

	@Test
	public void testPageAlwaysUTF8() {
		assertEquals("UTF-8", new OkPage("url", "content").getCharset());
	}
}
