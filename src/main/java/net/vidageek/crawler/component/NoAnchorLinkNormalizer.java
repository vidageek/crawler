package net.vidageek.crawler.component;

/**
 * @author jonasabreu
 */
public class NoAnchorLinkNormalizer implements LinkNormalizer {

	private final DefaultLinkNormalizer delegate;

	public NoAnchorLinkNormalizer(final String beginUrl) {
		delegate = new DefaultLinkNormalizer(beginUrl);
	}

	public String normalize(String url) {

		String normalizedUrl = delegate.normalize(url).toLowerCase();
		if (normalizedUrl.contains("#"))
			return normalizedUrl.substring(0, normalizedUrl.indexOf("#"));
		return normalizedUrl;
	}
}
