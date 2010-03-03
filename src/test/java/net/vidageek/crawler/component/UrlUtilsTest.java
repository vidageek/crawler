package net.vidageek.crawler.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Code copied from HtmlUnit
 * (src/main/java/com/gargoylesoftware/htmlunit/util/UrlUtils.java)
 * (https://htmlunit.svn.sourceforge.net/svnroot/htmlunit/trunk/htmlunit -
 * commit 5556)
 * 
 * Modifications made by jonasabreu (jonas at vidageek dot net)
 */
public class UrlUtilsTest {

	@Test
	public void resolveUrlWithNormalExamples() {
		final String baseUrl = "http://a/b/c/d;p?q#f";

		assertEquals("g:h", UrlUtils.resolveUrl(baseUrl, "g:h"));
		assertEquals("http://a/b/c/g", UrlUtils.resolveUrl(baseUrl, "g"));
		assertEquals("http://a/b/c/g", UrlUtils.resolveUrl(baseUrl, "./g"));
		assertEquals("http://a/b/c/g/", UrlUtils.resolveUrl(baseUrl, "g/"));
		assertEquals("http://a/g", UrlUtils.resolveUrl(baseUrl, "/g"));
		assertEquals("http://g", UrlUtils.resolveUrl(baseUrl, "//g"));
		assertEquals("http://a/b/c/d;p?y", UrlUtils.resolveUrl(baseUrl, "?y"));
		assertEquals("http://a/b/c/g?y", UrlUtils.resolveUrl(baseUrl, "g?y"));
		assertEquals("http://a/b/c/g?y/./x", UrlUtils.resolveUrl(baseUrl, "g?y/./x"));
		assertEquals("http://a/b/c/d;p?q#s", UrlUtils.resolveUrl(baseUrl, "#s"));
		assertEquals("http://a/b/c/g#s", UrlUtils.resolveUrl(baseUrl, "g#s"));
		assertEquals("http://a/b/c/g#s/./x", UrlUtils.resolveUrl(baseUrl, "g#s/./x"));
		assertEquals("http://a/b/c/g?y#s", UrlUtils.resolveUrl(baseUrl, "g?y#s"));
		assertEquals("http://a/b/c/d;x", UrlUtils.resolveUrl(baseUrl, ";x"));
		assertEquals("http://a/b/c/g;x", UrlUtils.resolveUrl(baseUrl, "g;x"));
		assertEquals("http://a/b/c/g;x?y#s", UrlUtils.resolveUrl(baseUrl, "g;x?y#s"));
		assertEquals("http://a/b/c/", UrlUtils.resolveUrl(baseUrl, "."));
		assertEquals("http://a/b/c/", UrlUtils.resolveUrl(baseUrl, "./"));
		assertEquals("http://a/b/", UrlUtils.resolveUrl(baseUrl, ".."));
		assertEquals("http://a/b/", UrlUtils.resolveUrl(baseUrl, "../"));
		assertEquals("http://a/b/g", UrlUtils.resolveUrl(baseUrl, "../g"));
		assertEquals("http://a/", UrlUtils.resolveUrl(baseUrl, "../.."));
		assertEquals("http://a/", UrlUtils.resolveUrl(baseUrl, "../../"));
		assertEquals("http://a/g", UrlUtils.resolveUrl(baseUrl, "../../g"));

	}

	@Test
	public void resolveUrlWithAbnormalExamples() {
		final String baseUrl = "http://a/b/c/d;p?q#f";

		assertEquals("http://a/b/c/d;p?q#f", UrlUtils.resolveUrl(baseUrl, ""));

		assertEquals("http://a/g", UrlUtils.resolveUrl(baseUrl, "../../../g"));
		assertEquals("http://a/g", UrlUtils.resolveUrl(baseUrl, "../../../../g"));
		assertEquals("http://a/./g", UrlUtils.resolveUrl(baseUrl, "/./g"));
		assertEquals("http://a/g", UrlUtils.resolveUrl(baseUrl, "/../g"));

		assertEquals("http://a/b/c/g.", UrlUtils.resolveUrl(baseUrl, "g."));
		assertEquals("http://a/b/c/.g", UrlUtils.resolveUrl(baseUrl, ".g"));
		assertEquals("http://a/b/c/g..", UrlUtils.resolveUrl(baseUrl, "g.."));
		assertEquals("http://a/b/c/..g", UrlUtils.resolveUrl(baseUrl, "..g"));
		assertEquals("http://a/b/g", UrlUtils.resolveUrl(baseUrl, "./../g"));
		assertEquals("http://a/b/c/g/", UrlUtils.resolveUrl(baseUrl, "./g/."));
		assertEquals("http://a/b/c/g/h", UrlUtils.resolveUrl(baseUrl, "g/./h"));
		assertEquals("http://a/b/c/h", UrlUtils.resolveUrl(baseUrl, "g/../h"));
		assertEquals("http:g", UrlUtils.resolveUrl(baseUrl, "http:g"));
		assertEquals("http:", UrlUtils.resolveUrl(baseUrl, "http:"));
	}

	@Test
	public void resolveUrlWithExtraExamples() {
		final String baseUrl = "http://a/b/c/d;p?q#f";

		assertEquals("http://a/b/c/d;", UrlUtils.resolveUrl(baseUrl, ";"));
		assertEquals("http://a/b/c/d;p?", UrlUtils.resolveUrl(baseUrl, "?"));
		assertEquals("http://a/b/c/d;p?q#", UrlUtils.resolveUrl(baseUrl, "#"));
		assertEquals("http://a/b/c/d;p?q#s", UrlUtils.resolveUrl(baseUrl, "#s"));

		assertEquals("http://a/f.html", UrlUtils.resolveUrl("http://a/otherFile.html", "../f.html"));
		assertEquals("http://a/f.html", UrlUtils.resolveUrl("http://a/otherFile.html", "../../f.html"));
	}
}
