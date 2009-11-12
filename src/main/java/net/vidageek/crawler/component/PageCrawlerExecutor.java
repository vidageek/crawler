package net.vidageek.crawler.component;

import java.util.concurrent.ConcurrentLinkedQueue;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.PageVisitor;
import net.vidageek.crawler.Status;

import org.apache.log4j.Logger;

/**
 * @author jonasabreu
 * 
 */
final public class PageCrawlerExecutor implements Runnable {

    private final ConcurrentLinkedQueue<String> urlsToCrawl;
    private final Downloader downloader;
    private final LinkNormalizer normalizer;
    private final PageVisitor visitor;
    private final ExecutorCounter counter;

    private final Logger log = Logger.getLogger(PageCrawlerExecutor.class);

    public PageCrawlerExecutor(final ExecutorCounter counter, final ConcurrentLinkedQueue<String> urlsToCrawl,
            final Downloader downloader, final LinkNormalizer normalizer, final PageVisitor visitor) {
        this.counter = counter;
        this.urlsToCrawl = urlsToCrawl;
        this.downloader = downloader;
        this.normalizer = normalizer;
        this.visitor = visitor;

        counter.increase();
    }

    public void run() {

        try {
            String urlToCrawl = urlsToCrawl.poll();
            if (urlToCrawl != null) {
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
                        urlsToCrawl.add(link);
                    }
                }
            }
        } finally {
            counter.decrease();
        }
    }

}
