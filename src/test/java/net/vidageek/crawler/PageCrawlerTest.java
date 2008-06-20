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
	PageVisitor visitor;

	@Before
	public void setUp() {
		mockery = new Mockery();
		downloader = mockery.mock(Downloader.class);
		visitor = mockery.mock(PageVisitor.class);
	}

	@After
	public void tearDown() {
		mockery.assertIsSatisfied();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThatThrowsExceptionIfUrlIsNull() {
		new PageCrawler(null, downloader);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThatThrowsExceptionIfUrlIsBlank() {
		new PageCrawler("   \n   \t   ", downloader);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThatThrowsExceptionIfUrlDoesntBeginWithHttp() {
		new PageCrawler("url", downloader);
	}

	@Test(expected = NullPointerException.class)
	public void testThatThrowsExceptionIfVisitorIsNull() {
		new PageCrawler("http://asdasdasdasdasd", downloader).craw(null);
	}

	@Test
	public void testThatOnlyFollowUrlsAuthorizedByPageVisitor() {

		mockery.checking(new Expectations() {
			{
				one(downloader).get("http://test.com");
				will(returnValue("<a href=\"http://link\">"));

				one(visitor).visit(with(any(Page.class)));

				one(visitor).followUrl(with(any(String.class)));
				will(returnValue(false));
			}
		});
		new PageCrawler("http://test.com", downloader).craw(visitor);
	}

	@Test
	public void testThatCrawlerAvoidCircles() {
		mockery.checking(new Expectations() {
			{
				one(downloader).get(with(any(String.class)));
				will(returnValue("<a href=\"http://test.com\"><a href=\"http://test.com\">"));

				one(visitor).visit(with(any(Page.class)));

				exactly(2).of(visitor).followUrl("http://test.com");
				will(returnValue(false));
			}
		});
		new PageCrawler("http://test.com", downloader).craw(visitor);
	}
}
