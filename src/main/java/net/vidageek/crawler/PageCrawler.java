/**
 * 
 */
package net.vidageek.crawler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author jonasabreu
 * 
 */
public class PageCrawler {

	private final String beginUrl;

	private static final Set<String> visitedUrls = new HashSet<String>();

	public PageCrawler(final String beginUrl) {
		if (beginUrl == null) {
			throw new IllegalArgumentException("beginUrl cannot be null");
		}
		if (beginUrl.trim().length() == 0) {
			throw new IllegalArgumentException("beginUrl cannot be empty");
		}
		if (!beginUrl.startsWith("http://")) {
			throw new IllegalArgumentException("beginUrl must start with http://");
		}
		this.beginUrl = beginUrl;
	}

	public void craw(final PageVisitor visitor) {
		if (visitor == null) {
			throw new NullPointerException("visitor cannot be null");
		}

		if (!visitedUrls.contains(this.beginUrl)) {
			visitedUrls.add(this.beginUrl);
			String page = this.downloadPage(this.beginUrl);
			visitor.visit(page);
			List<String> links = this.recoverUrls(page);
			for (String link : links) {
				new PageCrawler(link).craw(visitor);
			}

		}

	}

	private List<String> recoverUrls(final String page) {
		// TODO Auto-generated method stub
		return null;
	}

	private String downloadPage(final String beginUrl2) {
		// TODO Auto-generated method stub
		return null;
	}
}
