/**
 * 
 */
package net.vidageek.crawler;

import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * @author jonasabreu
 * 
 */

@RunWith(Theories.class)
public class PageTest {

	private Mockery mockery;
	Downloader downloader;

	@Before
	public void setup() {
		this.mockery = new Mockery();
		this.downloader = this.mockery.mock(Downloader.class);
	}

	@After
	public void tearDown() {
		this.mockery.assertIsSatisfied();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThatThrowsExceptionIfUrlIsNull() {
		new Page(null, this.downloader);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThatThrowsExceptionIfUrlIsEmpty() {
		new Page("  \n   \t   ", this.downloader);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThatThrowsExceptionIfDownloaderIsNull() {
		new Page("asdasdasd", null);
	}

	@Test
	public void testThatDownloaderIsCalledOnUrl() {
		this.mockery.checking(new Expectations() {
			{
				this.one(PageTest.this.downloader).get("defaultUrl");
				this.will(returnValue("<a href=\"test link\">"));
			}
		});

		List<String> links = new Page("defaultUrl", this.downloader).getLinks();

		assertEquals(1, links.size());
		assertEquals("test link", links.get(0));
	}

	@Test
	public void testThatCanRecoverTwoLinks() {
		this.mockery.checking(new Expectations() {
			{
				this.one(PageTest.this.downloader).get("defaultUrl");
				this.will(returnValue("<a href=\"test link\">\n\n <a \nhref=\"test link2\">"));
			}
		});

		List<String> links = new Page("defaultUrl", this.downloader).getLinks();

		assertEquals(2, links.size());
		assertEquals("test link", links.get(0));
		assertEquals("test link2", links.get(1));

	}
}
