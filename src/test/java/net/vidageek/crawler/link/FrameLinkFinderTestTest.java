package net.vidageek.crawler.link;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class FrameLinkFinderTestTest {

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentException() {
		new FrameLinkFinder(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyIllegalArgumentException() {
		new FrameLinkFinder("");
	}

	@Test
	public void testCanRecoverLinks() {
		List<String> links = new FrameLinkFinder(
				"<frameset rows=\"1,*\" frameborder=\"NO\" border=\"0\" framespacing=\"0\"><frame src=\"nada.html\" name=\"top\" scrolling=\"NO\" noresize ><frame src=\"redirect.html\" name=\"main\"></frameset>")
				.getLinks();
		Assert.assertEquals(2, links.size());
		Assert.assertEquals("nada.html", links.get(0));
		Assert.assertEquals("redirect.html", links.get(1));
	}
}
