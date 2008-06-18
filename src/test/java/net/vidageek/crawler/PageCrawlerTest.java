/**
 * 
 */
package net.vidageek.crawler;

import org.junit.Test;

/**
 * @author jonasabreu
 * 
 */
public class PageCrawlerTest {

	@Test(expected = IllegalArgumentException.class)
	public void testThatThrowsExceptionIfUrlIsNull() {
		new PageCrawler(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThatThrowsExceptionIfUrlIsBlank() {
		new PageCrawler("   \n   \t   ");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThatThrowsExceptionIfUrlDoesntBeginWithHttp() {
		new PageCrawler("url");
	}

	@Test(expected = NullPointerException.class)
	public void testThatThrowsExceptionIfVisitorIsNull() {
		new PageCrawler("http://asdasdasdasdasd").craw(null);
	}

}
