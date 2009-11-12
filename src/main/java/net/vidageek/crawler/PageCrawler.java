/**
 * 
 */
package net.vidageek.crawler;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.vidageek.crawler.component.DefaultLinkNormalizer;
import net.vidageek.crawler.component.Downloader;
import net.vidageek.crawler.component.ExecutorCounter;
import net.vidageek.crawler.component.LinkNormalizer;
import net.vidageek.crawler.component.PageCrawlerExecutor;
import net.vidageek.crawler.component.WebDownloader;
import net.vidageek.crawler.visitor.DoesNotFollowVisitedUrlVisitor;

/**
 * @author jonasabreu
 * 
 */
public class PageCrawler {

    private final String beginUrl;

    private final Downloader downloader;

    private final LinkNormalizer normalizer;

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
                new LinkedBlockingQueue<Runnable>(1200));

        monitorCrawling(executor, new DoesNotFollowVisitedUrlVisitor(beginUrl, visitor));

    }

    private void monitorCrawling(final ThreadPoolExecutor executor, final PageVisitor visitor) {
        final ExecutorCounter counter = new ExecutorCounter();
        ConcurrentLinkedQueue<String> urls = new ConcurrentLinkedQueue<String>();

        urls.add(beginUrl);

        while (true) {
            if ((urls.size() != 0) && (counter.value() < 1000)) {
                for (int i = 0; i < 100; i++) {
                    executor.execute(new PageCrawlerExecutor(counter, urls, downloader, normalizer, visitor));
                }
            } else {
                while (counter.value() != 0) {
                    Thread.yield();
                }
                if (urls.size() == 0) {
                    break;
                }
            }
            Thread.yield();

        }
    }
}
