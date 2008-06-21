package net.vidageek.crawler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.vidageek.crawler.exception.CrawlerException;

/**
 * @author jonasabreu
 */
public class LinkNormalizer {

	private final String beginUrl;

	public LinkNormalizer(final String beginUrl) {
		if (beginUrl == null || beginUrl.trim().length() == 0) {
			throw new IllegalArgumentException(
					"beginUrl cannot be null or empty");
		}

		if (!beginUrl.startsWith("http://")) {
			throw new CrawlerException("beginUrl must start with http://");
		}
		if (beginUrl.endsWith("/")) {
			this.beginUrl = beginUrl;
		} else {
			this.beginUrl = beginUrl + "/";
		}
	}

	public String normalize(final String url) {
		if (url.startsWith("http://")) {
			return url;
		}
		String normalized = beginUrl + url;
		Pattern pattern = Pattern.compile("(/[^/]+/\\.\\.)");
		Matcher matcher = pattern.matcher(normalized);
		while (matcher.find()) {
			normalized = normalized.replaceFirst(Pattern
					.quote(matcher.group(0)), "");
			matcher = pattern.matcher(normalized);
		}

		return normalized;
	}
}
