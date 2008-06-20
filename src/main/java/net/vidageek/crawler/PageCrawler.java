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

	private final Downloader downloader;

	public PageCrawler(final String beginUrl) {
		this(beginUrl, new WebDownloader());
	}

	public PageCrawler(final String beginUrl, final Downloader downloader) {
		this.downloader = downloader;
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

	private void craw(final PageVisitor visitor, final Set<String> visitedUrls) {
		if (visitor == null) {
			throw new NullPointerException("visitor cannot be null");
		}

		if (!visitedUrls.contains(beginUrl)) {
			visitedUrls.add(beginUrl);
			Page page = new Page(beginUrl, downloader);
			visitor.visit(page);

			List<String> links = page.getLinks();
			for (String link : links) {
				if (visitor.followUrl(link)) {
					new PageCrawler(link, downloader).craw(visitor);
				}
			}

		}

	}

	public void craw(final PageVisitor visitor) {
		craw(visitor, new HashSet<String>());

	}
}
