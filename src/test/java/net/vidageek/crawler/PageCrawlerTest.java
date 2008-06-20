/**
 * 
 */
package net.vidageek.crawler;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jonasabreu
 * 
 */
public class PageCrawlerTest {

	private Mockery mockery;
	Downloader downloader;

	@Before
	public void setUp() {
		this.mockery = new Mockery();
		this.downloader = this.mockery.mock(Downloader.class);
	}

	@After
	public void tearDown() {
		this.mockery.assertIsSatisfied();
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

	@Test
	public void testThatOnlyFollowUrlsAuthorizedByPageVisitor() {
		final PageVisitor visitor = this.mockery.mock(PageVisitor.class);

		this.mockery.checking(new Expectations() {
			{
				this.one(PageCrawlerTest.this.downloader).get("http://test.com");
				this.will(returnValue("<a href=\"http://link\">"));

				this.one(visitor).visit(this.with(any(Page.class)));

				this.one(visitor).followUrl(this.with(any(String.class)));
				this.will(returnValue(false));
			}
		});
		new PageCrawler("http://test.com", this.downloader).craw(visitor);
	}
}
