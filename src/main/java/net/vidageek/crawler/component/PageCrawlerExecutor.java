package net.vidageek.crawler.component;

import java.util.concurrent.ThreadPoolExecutor;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.PageCrawler;
import net.vidageek.crawler.PageVisitor;
import net.vidageek.crawler.Status;

import org.apache.log4j.Logger;

/**
 * @author jonasabreu
 * 
 */
final public class PageCrawlerExecutor implements Runnable {

    private final ThreadPoolExecutor executor;
    private final String urlToCrawl;
    private final Downloader downloader;
    private final LinkNormalizer normalizer;
    private final PageVisitor visitor;
    private final ExecutorCounter counter;

    private final Logger log = Logger.getLogger(PageCrawler.class);

    public PageCrawlerExecutor(final ThreadPoolExecutor executor, final ExecutorCounter counter,
            final String urlToCrawl, final Downloader downloader, final LinkNormalizer normalizer,
            final PageVisitor visitor) {
        counter.increase();
        this.executor = executor;
        this.counter = counter;
        this.urlToCrawl = urlToCrawl;
        this.downloader = downloader;
        this.normalizer = normalizer;
        this.visitor = visitor;
    }

    public void run() {

        log.info("crawling url: " + urlToCrawl);

        Page page = downloader.get(urlToCrawl);
        if (page.getStatusCode() != Status.OK) {
            visitor.onError(urlToCrawl, page.getStatusCode());
        } else {
            visitor.visit(page);
        }

        for (String l : page.getLinks()) {
            String link = normalizer.normalize(l);
            if (visitor.followUrl(link)) {
                executor.execute(new PageCrawlerExecutor(executor, counter, link, downloader, normalizer, visitor));
            }
        }
        counter.decrease();
    }

}
