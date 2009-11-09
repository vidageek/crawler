package net.vidageek.crawler.link;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class DefaultLinkTest {

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentException() {
		new DefaultLink(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyIllegalArgumentException() {
		new DefaultLink("");
	}

	@Test
	public void testCanRecoverLinks() {
		List<String> links = new DefaultLink(
				"<br /> <a id=\"link1\" href=\"test.page1\"></a><br />  <a id=\"link2\" href=\"test.page2\"></a>")
				.getLinks();
		Assert.assertEquals(2, links.size());
		Assert.assertEquals("test.page1", links.get(0));
		Assert.assertEquals("test.page2", links.get(1));
	}
}
