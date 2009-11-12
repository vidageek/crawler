package net.vidageek.crawler.component;

import java.util.concurrent.ThreadPoolExecutor;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.PageVisitor;
import net.vidageek.crawler.Status;
import net.vidageek.crawler.Url;

import org.apache.log4j.Logger;

/**
 * @author jonasabreu
 * 
 */
final public class PageCrawlerExecutor implements Runnable {

    private final Downloader downloader;
    private final LinkNormalizer normalizer;
    private final PageVisitor visitor;
    private final ExecutorCounter counter;

    private final Logger log = Logger.getLogger(PageCrawlerExecutor.class);
    private final Url urlToCrawl;
    private final ThreadPoolExecutor executor;

    public PageCrawlerExecutor(final Url urlToCrawl, final ThreadPoolExecutor executor, final ExecutorCounter counter,
            final Downloader downloader, final LinkNormalizer normalizer, final PageVisitor visitor) {
        this.urlToCrawl = urlToCrawl;
        this.executor = executor;
        this.counter = counter;
        this.downloader = downloader;
        this.normalizer = normalizer;
        this.visitor = visitor;

        counter.increase();
    }

    public void run() {
        try {

            log.info("crawling url: " + urlToCrawl.link());

            Page page = downloader.get(urlToCrawl.link());
            if (page.getStatusCode() != Status.OK) {
                visitor.onError(urlToCrawl, page.getStatusCode());
            } else {
                visitor.visit(page);
            }

            for (String l : page.getLinks()) {
                String link = normalizer.normalize(l);
                final Url url = new Url(link, urlToCrawl.depth() + 1);
                if (visitor.followUrl(url)) {
                    executor.execute(new PageCrawlerExecutor(url, executor, counter, downloader, normalizer, visitor));
                }
            }

        } finally {
            counter.decrease();
        }
    }

}
