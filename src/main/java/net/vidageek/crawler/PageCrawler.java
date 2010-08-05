/**
 * 
 */
package net.vidageek.crawler;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.vidageek.crawler.component.ExecutorCounter;
import net.vidageek.crawler.component.PageCrawlerExecutor;
import net.vidageek.crawler.config.CrawlerConfiguration;
import net.vidageek.crawler.exception.CrawlerException;
import net.vidageek.crawler.visitor.DoesNotFollowVisitedUrlVisitor;

import org.apache.log4j.Logger;

/**
 * @author jonasabreu
 * 
 */
public class PageCrawler {

	private final Logger log = Logger.getLogger(PageCrawler.class);

	private final CrawlerConfiguration config;

	public PageCrawler(final CrawlerConfiguration config) {
		this.config = config;
	}

	public PageCrawler(final String beginUrl) {
		this(new CrawlerConfiguration(beginUrl));
	}

	public void crawl(final PageVisitor visitor) {
		if (visitor == null) {
			throw new NullPointerException("visitor cannot be null");
		}

		ThreadPoolExecutor executor = new ThreadPoolExecutor(config.minPoolSize(), config.maxPoolSize(), config
				.keepAliveMilliseconds(), TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

		final ExecutorCounter counter = new ExecutorCounter();

		try {
			executor
					.execute(new PageCrawlerExecutor(new Url(config.beginUrl(), 0), executor, counter, config
							.downloader(), config.normalizer(), new DoesNotFollowVisitedUrlVisitor(config.beginUrl(),
							visitor)));

			while (counter.value() != 0) {
				log.debug("executors that finished: " + executor.getCompletedTaskCount());
				log.debug("Number of Executors alive: " + counter.value());
				sleep();
			}
		} finally {
			executor.shutdown();
		}
	}

	private void sleep() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			throw new CrawlerException("main thread died. ", e);
		}

	}
}
