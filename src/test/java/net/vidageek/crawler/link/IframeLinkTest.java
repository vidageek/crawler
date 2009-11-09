package net.vidageek.crawler.link;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class IframeLinkTest {

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentException() {
		new IframeLink(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyIllegalArgumentException() {
		new IframeLink("");
	}

	@Test
	public void testCanRecoverLinks() {
		List<String> links = new IframeLink(
				"<br /> <iframe id=\"link1\" href=\"test.page1\"></iframe><br />  <iframe id=\"link2\" href=\"test.page2\"></iframe>")
				.getLinks();
		Assert.assertEquals(2, links.size());
		Assert.assertEquals("test.page1", links.get(0));
		Assert.assertEquals("test.page2", links.get(1));
	}
}
