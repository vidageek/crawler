/**
 * 
 */
package net.vidageek.crawler;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.vidageek.crawler.component.DefaultLinkNormalizer;
import net.vidageek.crawler.component.Downloader;
import net.vidageek.crawler.component.ExecutorCounter;
import net.vidageek.crawler.component.LinkNormalizer;
import net.vidageek.crawler.component.PageCrawlerExecutor;
import net.vidageek.crawler.component.WebDownloader;
import net.vidageek.crawler.exception.CrawlerException;
import net.vidageek.crawler.visitor.DoesNotFollowVisitedUrlVisitor;

import org.apache.log4j.Logger;

/**
 * @author jonasabreu
 * 
 */
public class PageCrawler {

    private final String beginUrl;

    private final Downloader downloader;

    private final LinkNormalizer normalizer;

    private final Logger log = Logger.getLogger(PageCrawler.class);

    public PageCrawler(final String beginUrl) {
        this(beginUrl, new WebDownloader(), new DefaultLinkNormalizer(beginUrl));
    }

    public PageCrawler(final String beginUrl, final Downloader downloader, final LinkNormalizer normalizer) {
        if (beginUrl == null) {
            throw new IllegalArgumentException("beginUrl cannot be null");
        }
        if (beginUrl.trim().length() == 0) {
            throw new IllegalArgumentException("beginUrl cannot be empty");
        }
        if (!beginUrl.startsWith("http://")) {
            throw new IllegalArgumentException("beginUrl must start with http://");
        }
        this.beginUrl = beginUrl;
        this.downloader = downloader;
        this.normalizer = normalizer;
    }

    public void crawl(final PageVisitor visitor) {
        if (visitor == null) {
            throw new NullPointerException("visitor cannot be null");
        }

        ThreadPoolExecutor executor = new ThreadPoolExecutor(30, 30, 30, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>());

        final ExecutorCounter counter = new ExecutorCounter();

        try {
            executor.execute(new PageCrawlerExecutor(new Url(beginUrl, 0), executor, counter, downloader, normalizer,
                    new DoesNotFollowVisitedUrlVisitor(beginUrl, visitor)));

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
