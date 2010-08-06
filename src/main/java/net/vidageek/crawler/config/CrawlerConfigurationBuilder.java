package net.vidageek.crawler.config;

import net.vidageek.crawler.component.Downloader;
import net.vidageek.crawler.component.LinkNormalizer;

/**
 * WARNING: This class is not thread-safe.
 * 
 * @author jonasabreu
 * 
 */
final public class CrawlerConfigurationBuilder {

	private final CrawlerConfiguration configuration;

	public CrawlerConfigurationBuilder(final String beginUrl) {
		configuration = new CrawlerConfiguration(beginUrl);
	}

	public CrawlerConfigurationBuilder withDownloader(final Downloader downloader) {
		if (downloader == null) {
			throw new IllegalArgumentException("downloader cannot be null");
		}
		configuration.downloader(downloader);
		return this;
	}

	public CrawlerConfigurationBuilder withLinkNormalizer(final LinkNormalizer normalizer) {
		if (normalizer == null) {
			throw new IllegalArgumentException("link normalizer cannot be null");
		}
		configuration.linkNormalizer(normalizer);
		return this;
	}

	public CrawlerConfigurationBuilder withKeepAlive(final int keepAliveMilliseconds) {
		configuration.keepAliveMilliseconds(keepAliveMilliseconds);
		return this;
	}

	public CrawlerConfigurationBuilder withRequestDelay(final int requestDelayMilliseconds) {
		configuration.requestDelayMilliseconds(requestDelayMilliseconds);
		return this;
	}

	public CrawlerConfiguration build() {
		return configuration;
	}

	public CrawlerConfigurationBuilder withMaxParallelRequests(final int numberOfRequests) {
		configuration.maxPoolSize(numberOfRequests);
		configuration.minPoolSize(numberOfRequests);
		return this;
	}
}
