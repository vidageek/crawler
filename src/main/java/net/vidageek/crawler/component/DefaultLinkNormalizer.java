package net.vidageek.crawler.component;

import net.vidageek.crawler.exception.CrawlerException;

/**
 * @author jonasabreu
 */
public class DefaultLinkNormalizer implements LinkNormalizer {

	private final String beginUrl;

	public DefaultLinkNormalizer(final String beginUrl) {
		if ((beginUrl == null) || (beginUrl.trim().length() == 0)) {
			throw new IllegalArgumentException("beginUrl cannot be null or empty");
		}

		if (!beginUrl.startsWith("http://")) {
			throw new CrawlerException("beginUrl must start with http://");
		}
		this.beginUrl = beginUrl;
	}

	public String normalize(String url) {

		url = url.replaceAll("&amp;", "&");

		return UrlUtils.resolveUrl(beginUrl, url);
	}
}
