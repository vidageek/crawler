package net.vidageek.crawler.page;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.Status;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests to {@link DefaultPageFactory}.
 */
public class DefaultPageFactoryTest {

	private PageFactory factory;

	@Before
	public void setUp() throws Exception {
		factory = new DefaultPageFactory();
	}

	@Test
	public void shouldBuildOkPage() throws Exception {
		String url = "http://test.com";
		String content = "<html></html>";

		Page okPage= factory.buildOkPage(url, content);

		assertThat(okPage, instanceOf(OkPage.class));
		assertThat(okPage.getUrl(), is(url));
		assertThat(okPage.getContent(), is(content));
	}

	@Test
	public void shouldBuildErrorPage() throws Exception {
		String url = "http://test.com";
		Status status = Status.INTERNAL_SERVER_ERROR;

		Page errorPage= factory.buildErrorPage(url, status);

		assertThat(errorPage, instanceOf(ErrorPage.class));
		assertThat(errorPage.getUrl(), is(url));
		assertThat(errorPage.getStatusCode(), is(status));
	}

	@Test
	public void shouldBuildRejectedMimeTypePage() throws Exception {
		String url = "http://test.com";
		Status status = Status.INTERNAL_SERVER_ERROR;
		String mimeType = "text/html";

		Page rejectedMimeTypePage= factory.buildRejectedMimeTypePage(url, status, mimeType);

		assertThat(rejectedMimeTypePage, instanceOf(RejectedMimeTypePage.class));
		RejectedMimeTypePage returned = (RejectedMimeTypePage) rejectedMimeTypePage;
		assertThat(returned.getUrl(), is(url));
		assertThat(returned.getStatusCode(), is(status));
		assertThat(returned.getMimeType(), is(mimeType));
	}
}
