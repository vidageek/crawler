package net.vidageek.crawler.component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.vidageek.crawler.exception.CrawlerException;

/**
 * @author jonasabreu
 */
public class DefaultLinkNormalizer implements LinkNormalizer {

	private final String beginUrl;

	public DefaultLinkNormalizer(final String beginUrl) {
		if (beginUrl == null || beginUrl.trim().length() == 0) {
			throw new IllegalArgumentException("beginUrl cannot be null or empty");
		}

		if (!beginUrl.startsWith("http://")) {
			throw new CrawlerException("beginUrl must start with http://");
		}
		this.beginUrl = beginUrl;
	}

	public String normalize(final String url) {
		if (url.startsWith("http://") || url.startsWith("https://")) {
			return url;
		}

		if (url.startsWith("#")) {
			return beginUrl;
		}

		if (url.startsWith("/")) {
			Matcher matcher = Pattern.compile("(http://[^/]+)").matcher(beginUrl);
			if (matcher.find()) {
				return matcher.group(1) + url;
			}
		}

		String normalized = null;
		if (beginUrl.endsWith("/")) {
			normalized = beginUrl + url;
		} else {
			String substring = beginUrl.substring(beginUrl.lastIndexOf("/") + 1);
			if (substring.contains(".")) {
				normalized = beginUrl.subSequence(0, beginUrl.length() - substring.length()) + url;
			} else {
				normalized = beginUrl + "/" + url;
			}
		}
		Matcher matcher = Pattern.compile("(/[^/]+/\\.\\.)").matcher(normalized);
		while (matcher.find()) {
			normalized = normalized.replaceFirst(Pattern.quote(matcher.group(0)), "");
			matcher = Pattern.compile("(/[^/]+/\\.\\.)").matcher(normalized);
		}

		return normalized;
	}
}
