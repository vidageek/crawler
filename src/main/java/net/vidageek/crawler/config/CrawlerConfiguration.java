package net.vidageek.crawler.config;

import net.vidageek.crawler.component.Downloader;
import net.vidageek.crawler.component.LinkNormalizer;

/**
 * @author jonasabreu
 * 
 */
final public class CrawlerConfiguration {

	private final String beginUrl;

	public CrawlerConfiguration(final String beginUrl) {
		this.beginUrl = beginUrl;
	}

	public String beginUrl() {
		return beginUrl;
	}

	public Downloader downloader() {
		return null;
	}

	public LinkNormalizer normalizer() {
		// TODO Auto-generated method stub
		return null;
	}

	public int minPoolSize() {
		return 30;
	}

	public int maxPoolSize() {
		return 30;
	}

	public long keepAliveMilliseconds() {
		return 30000;
	}

}
