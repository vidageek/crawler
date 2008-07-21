/**
 * 
 */
package net.vidageek.crawler.visitor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.PageVisitor;
import net.vidageek.crawler.StatusError;

/**
 * Simple visitor to crawl a single domain.
 * 
 * @author jonasabreu
 * 
 */
public class DomainVisitor implements PageVisitor {

	private final String domain;

	public DomainVisitor(final String baseUrl) {
		if (baseUrl == null || baseUrl.trim().length() == 0) {
			throw new IllegalArgumentException("baseUrl cannot be null or empty");
		}
		Matcher matcher = Pattern.compile("(http://[^/]+)").matcher(baseUrl);
		if (!matcher.find()) {
			throw new IllegalArgumentException("baseUrl must match http://[^/]+");
		}
		domain = matcher.group(1) + "/";
	}

	@Override
	public boolean followUrl(final String url) {
		if (url == null) {
			return false;
		}
		return url.startsWith(domain);
	}

	@Override
	public void onError(final String url, final StatusError statusError) {
		// do nothing
	}

	@Override
	public void visit(final Page page) {
		// do nothing
	}

}
