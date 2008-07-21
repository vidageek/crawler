/**
 * 
 */
package net.vidageek.crawler;

import net.vidageek.crawler.component.ComponentFactory;
import net.vidageek.crawler.component.DefaultLinkNormalizer;
import net.vidageek.crawler.component.Downloader;
import net.vidageek.crawler.component.LinkNormalizer;

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
	ComponentFactory factory;

	@Before
	public void setUp() {
		mockery = new Mockery();
		downloader = mockery.mock(Downloader.class);
		visitor = mockery.mock(PageVisitor.class);
		factory = mockery.mock(ComponentFactory.class);

		mockery.checking(new Expectations() {
			{
				allowing(factory).createComponent(Downloader.class);
				will(returnValue(downloader));
			}
		});
	}

	@After
	public void tearDown() {
		mockery.assertIsSatisfied();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThatThrowsExceptionIfUrlIsNull() {
		new PageCrawler(null, factory);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThatThrowsExceptionIfUrlIsBlank() {
		new PageCrawler("   \n   \t   ", factory);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThatThrowsExceptionIfUrlDoesntBeginWithHttp() {
		new PageCrawler("url", factory);
	}

	@Test(expected = NullPointerException.class)
	public void testThatThrowsExceptionIfVisitorIsNull() {
		new PageCrawler("http://asdasdasdasdasd", factory).crawl(null);
	}

	@Test
	public void testThatOnlyFollowUrlsAuthorizedByPageVisitor() {

		mockery.checking(new Expectations() {
			{
				allowing(downloader).getErrorCode();
				will(returnValue(StatusError.OK));

				one(downloader).get("http://test.com");
				will(returnValue("<a href=\"http://link\">"));

				allowing(factory).createComponent(LinkNormalizer.class, "http://test.com");
				will(returnValue(new DefaultLinkNormalizer("http://test.com")));

				one(visitor).visit(with(any(Page.class)));

				one(visitor).followUrl(with(any(String.class)));
				will(returnValue(false));
			}
		});
		new PageCrawler("http://test.com", factory).crawl(visitor);
	}

	@Test
	public void testThatCrawlerAvoidCircles() {
		mockery.checking(new Expectations() {
			{
				allowing(downloader).getErrorCode();
				will(returnValue(StatusError.OK));

				one(downloader).get(with(any(String.class)));
				will(returnValue("<a href=\"http://test.com\"><a href=\"http://test.com\">"));

				allowing(factory).createComponent(LinkNormalizer.class, "http://test.com");
				will(returnValue(new DefaultLinkNormalizer("http://test.com")));

				one(visitor).visit(with(any(Page.class)));

				exactly(2).of(visitor).followUrl("http://test.com");
				will(returnValue(false));
			}
		});
		new PageCrawler("http://test.com", factory).crawl(visitor);
	}

}
