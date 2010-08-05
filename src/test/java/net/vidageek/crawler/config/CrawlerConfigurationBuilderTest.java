package net.vidageek.crawler.config;

import org.junit.Test;

/**
 * @author jonasabreu
 * 
 */
final public class CrawlerConfigurationBuilderTest {

	@Test(expected = IllegalArgumentException.class)
	public void testThatWithDownloaderThrowsExceptionIfDownloaderIsNull() {
		CrawlerConfiguration.forStartPoint("http://www").withDownloader(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThatWithLinkNormalizerThrowsExceptionIfNormalizerIsNull() {
		CrawlerConfiguration.forStartPoint("http://www").withLinkNormalizer(null);
	}

}
