/**
 * 
 */
package net.vidageek.crawler;

import static org.junit.Assert.assertEquals;
import net.vidageek.crawler.component.DefaultLinkNormalizer;
import net.vidageek.crawler.exception.CrawlerException;

import org.junit.Test;

/**
 * @author jonasabreu
 * 
 */
public class DefaultLinkNormalizerTest {

	@Test(expected = IllegalArgumentException.class)
	public void testThatThrowsExceptionIfUrlIsNull() {
		new DefaultLinkNormalizer(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThatThrowsExceptionIfUrlIsEmpty() {
		new DefaultLinkNormalizer("  \n  \t  ");
	}

	@Test(expected = CrawlerException.class)
	public void testThatThrowsExceptionIfUrlDoenstBeginsWithHttp() {
		new DefaultLinkNormalizer("something just to break");
	}

	@Test
	public void testThatDoesntNormalizeIfStartsWithHttp() {
		String url = new DefaultLinkNormalizer("http://test.com/foo").normalize("http://other.com/bar");

		assertEquals("http://other.com/bar", url);
	}

	@Test
	public void testThatDoesntNormalizeIfStartsWithHttps() {
		String url = new DefaultLinkNormalizer("http://test.com/foo").normalize("https://other.com/bar");

		assertEquals("https://other.com/bar", url);
	}

	@Test
	public void testThatNormalizesUrl1() {
		String url = new DefaultLinkNormalizer("http://test.com/foo").normalize("../bar");

		assertEquals("http://test.com/bar", url);
	}

	@Test
	public void testThatNormalizesUrl2() {
		String url = new DefaultLinkNormalizer("http://test.com/foo.html").normalize("bar.html");

		assertEquals("http://test.com/bar.html", url);
	}

	@Test
	public void testThatNormalizesUrl3() {
		String url = new DefaultLinkNormalizer("http://test.com/foo.html").normalize("bar/foo.html");

		assertEquals("http://test.com/bar/foo.html", url);
	}

	@Test
	public void testThatNormalizesUrl5() {
		String url = new DefaultLinkNormalizer("http://test.com/foo").normalize("/bar.html");

		assertEquals("http://test.com/bar.html", url);
	}

	@Test
	public void testThatNormalizesUrl6() {
		String url = new DefaultLinkNormalizer("http://test.com/foo/bar").normalize("../../bar.html");

		assertEquals("http://test.com/bar.html", url);
	}

	@Test
	public void testThatReplaceEntityAmpForAmp() {
		String normalize = new DefaultLinkNormalizer("http://www.test.com").normalize("/web?param=1&amp;param2=2");

		assertEquals("http://www.test.com/web?param=1&param2=2", normalize);

	}

}
