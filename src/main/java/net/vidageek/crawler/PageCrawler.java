/**
 * 
 */
package net.vidageek.crawler;

import java.util.HashSet;
import java.util.Set;

import net.vidageek.crawler.component.DefaultLinkNormalizer;
import net.vidageek.crawler.component.Downloader;
import net.vidageek.crawler.component.LinkNormalizer;
import net.vidageek.crawler.component.WebDownloader;

import org.apache.log4j.Logger;

/**
 * @author jonasabreu
 * 
 */
public class PageCrawler {

    private final String beginUrl;

    private final Downloader downloader;

    private final Logger LOG = Logger.getLogger(PageCrawler.class);

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

    private void crawl(final PageVisitor visitor, final Set<String> visitedUrls) {
        if (visitor == null) {
            throw new NullPointerException("visitor cannot be null");
        }

        if (!visitedUrls.contains(beginUrl)) {
            LOG.info("crawling url: " + beginUrl);
            visitedUrls.add(beginUrl);
            Page page = downloader.get(beginUrl);
            if (page.getStatusCode() != Status.OK) {
                visitor.onError(beginUrl, page.getStatusCode());
            } else {
                visitor.visit(page);
            }

            for (String l : page.getLinks()) {
                String link = normalizer.normalize(l);
                if (visitor.followUrl(link)) {
                    new PageCrawler(link, downloader, normalizer).crawl(visitor, visitedUrls);
                }
            }

        }

    }

    public void crawl(final PageVisitor visitor) {
        crawl(visitor, new HashSet<String>());
    }
}
