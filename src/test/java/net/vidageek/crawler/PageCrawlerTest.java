/**
 * 
 */
package net.vidageek.crawler;

import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jonasabreu
 * 
 */
public class PageCrawlerTest {

	private Mockery mockery;
	private Downloader downloader;

	@Before
	public void setUp() {
		this.mockery = new Mockery();
		this.downloader = this.mockery.mock(Downloader.class);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThatThrowsExceptionIfUrlIsNull() {
		new PageCrawler(null, this.downloader);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThatThrowsExceptionIfUrlIsBlank() {
		new PageCrawler("   \n   \t   ", this.downloader);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThatThrowsExceptionIfUrlDoesntBeginWithHttp() {
		new PageCrawler("url", this.downloader);
	}

	@Test(expected = NullPointerException.class)
	public void testThatThrowsExceptionIfVisitorIsNull() {
		new PageCrawler("http://asdasdasdasdasd", this.downloader).craw(null);
	}

}
