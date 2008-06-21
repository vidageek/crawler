/**
 * 
 */
package net.vidageek.crawler;

import static org.junit.Assert.assertEquals;
import net.vidageek.crawler.exception.CrawlerException;

import org.junit.Test;

/**
 * @author jonasabreu
 * 
 */
public class LinkNormalizerTest {

	@Test(expected = IllegalArgumentException.class)
	public void testThatThrowsExceptionIfUrlIsNull() {
		new LinkNormalizer(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThatThrowsExceptionIfUrlIsEmpty() {
		new LinkNormalizer("  \n  \t  ");
	}

	@Test(expected = CrawlerException.class)
	public void testThatThrowsExceptionIfUrlDoenstBeginsWithHttp() {
		new LinkNormalizer("something just to break");
	}

	@Test
	public void testThatNormalizesUrl() {
		String url = new LinkNormalizer("http://test.com/foo")
				.normalize("../bar");

		assertEquals("http://test.com/bar", url);
	}

	@Test
	public void testThatDoesntNormalizeIfStartsWithHttp() {
		String url = new LinkNormalizer("http://test.com/foo")
				.normalize("http://other.com/bar");

		assertEquals("http://other.com/bar", url);
	}

}
