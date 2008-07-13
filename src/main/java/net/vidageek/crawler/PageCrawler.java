/**
 * 
 */
package net.vidageek.crawler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.vidageek.crawler.component.ComponentFactory;
import net.vidageek.crawler.component.DefaultComponentFactory;
import net.vidageek.crawler.component.Downloader;
import net.vidageek.crawler.component.LinkNormalizer;

/**
 * @author jonasabreu
 * 
 */
public class PageCrawler {

	private final String beginUrl;

	private final ComponentFactory factory;

	private final Downloader downloader;

	public PageCrawler(final String beginUrl) {
		this(beginUrl, new DefaultComponentFactory());
	}

	public PageCrawler(final String beginUrl, final ComponentFactory factory) {
		if (beginUrl == null) {
			throw new IllegalArgumentException("beginUrl cannot be null");
		}
		if (beginUrl.trim().length() == 0) {
			throw new IllegalArgumentException("beginUrl cannot be empty");
		}
		if (!beginUrl.startsWith("http://")) {
			throw new IllegalArgumentException("beginUrl must start with http://");
		}
		if (factory == null) {
			throw new IllegalArgumentException("factory cannot be null");
		}
		this.factory = factory;
		this.beginUrl = beginUrl;
		downloader = factory.createComponent(Downloader.class);
	}

	private void crawl(final PageVisitor visitor, final Set<String> visitedUrls) {
		if (visitor == null) {
			throw new NullPointerException("visitor cannot be null");
		}

		if (!visitedUrls.contains(beginUrl)) {
			visitedUrls.add(beginUrl);
			Page page = new Page(beginUrl, downloader);
			visitor.visit(page);
			LinkNormalizer normalizer = factory.createComponent(LinkNormalizer.class, beginUrl);

			List<String> links = page.getLinks();
			for (String l : links) {
				String link = normalizer.normalize(l);
				if (visitor.followUrl(link)) {
					new PageCrawler(link, factory).crawl(visitor, visitedUrls);
				}
			}

		}

	}

	public void crawl(final PageVisitor visitor) {
		crawl(visitor, new HashSet<String>());

	}
}
