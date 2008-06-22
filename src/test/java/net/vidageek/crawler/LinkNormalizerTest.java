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
	public void testThatDoesntNormalizeIfStartsWithHttp() {
		String url = new LinkNormalizer("http://test.com/foo")
				.normalize("http://other.com/bar");

		assertEquals("http://other.com/bar", url);
	}

	@Test
	public void testThatNormalizesUrl1() {
		String url = new LinkNormalizer("http://test.com/foo")
				.normalize("../bar");

		assertEquals("http://test.com/bar", url);
	}

	@Test
	public void testThatNormalizesUrl2() {
		String url = new LinkNormalizer("http://test.com/foo.html")
				.normalize("bar.html");

		assertEquals("http://test.com/bar.html", url);
	}

	@Test
	public void testThatNormalizesUrl3() {
		String url = new LinkNormalizer("http://test.com/foo.html")
				.normalize("bar/foo.html");

		assertEquals("http://test.com/bar/foo.html", url);
	}

	@Test
	public void testThatNormalizesUrl4() {
		String url = new LinkNormalizer("http://test.com/foo")
				.normalize("bar.html");

		assertEquals("http://test.com/foo/bar.html", url);
	}

	@Test
	public void testThatNormalizesUrl5() {
		String url = new LinkNormalizer("http://test.com/foo")
				.normalize("/bar.html");

		assertEquals("http://test.com/bar.html", url);
	}

	@Test
	public void testThatNormalizesUrl6() {
		String url = new LinkNormalizer("http://test.com/foo/bar")
				.normalize("../../bar.html");

		assertEquals("http://test.com/bar.html", url);
	}

}
