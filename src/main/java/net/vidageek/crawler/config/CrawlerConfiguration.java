package net.vidageek.crawler.config;

import java.util.regex.Pattern;

import net.vidageek.crawler.component.DefaultLinkNormalizer;
import net.vidageek.crawler.component.Downloader;
import net.vidageek.crawler.component.LinkNormalizer;
import net.vidageek.crawler.component.WebDownloader;

/**
 * @author jonasabreu
 * 
 */
final public class CrawlerConfiguration {

	private final String beginUrl;
	private Downloader downloader;
	private LinkNormalizer normalizer;
	private int minPoolSize;
	private int maxPoolSize;
	private long keepAliveMilliseconds;
	private int requestDelayMilliseconds;

	public CrawlerConfiguration(final String beginUrl) {
		if ((beginUrl == null) || (beginUrl.trim().length() == 0)) {
			throw new IllegalArgumentException("beginUrl cannot be null or empty");
		}
		if (!Pattern.compile("(?s)^http[s]?://.*$").matcher(beginUrl).matches()) {
			throw new IllegalArgumentException("beginUrl must start with http:// or https://");
		}
		this.beginUrl = beginUrl;
		downloader = new WebDownloader();
		normalizer = new DefaultLinkNormalizer(beginUrl);
		minPoolSize = 30;
		maxPoolSize = 30;
		keepAliveMilliseconds = 30000;
		requestDelayMilliseconds = 1000;
	}

	public String beginUrl() {
		return beginUrl;
	}

	public Downloader downloader() {
		return downloader;
	}

	public LinkNormalizer normalizer() {
		return normalizer;
	}

	public int minPoolSize() {
		return minPoolSize;
	}

	public int maxPoolSize() {
		return maxPoolSize;
	}

	public long keepAliveMilliseconds() {
		return keepAliveMilliseconds;
	}

	public int requestDelayMilliseconds() {
		return requestDelayMilliseconds;
	}

	public static CrawlerConfigurationBuilder forStartPoint(final String beginUrl) {
		return new CrawlerConfigurationBuilder(beginUrl);
	}

	public void downloader(final Downloader downloader) {
		this.downloader = downloader;
	}

	public void linkNormalizer(final LinkNormalizer normalizer) {
		this.normalizer = normalizer;
	}

	public void minPoolSize(final int minPoolSize) {
		this.minPoolSize = minPoolSize;
	}

	public void maxPoolSize(final int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;

	}

	public void keepAliveMilliseconds(final int keepAliveMilliseconds) {
		this.keepAliveMilliseconds = keepAliveMilliseconds;

	}

	public void requestDelayMilliseconds(final int requestDelayMilliseconds) {
		this.requestDelayMilliseconds = requestDelayMilliseconds;
	}

}
