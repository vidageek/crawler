package net.vidageek.crawler.visitor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.PageVisitor;
import net.vidageek.crawler.Status;
import net.vidageek.crawler.Url;

/**
 * @author jonasabreu
 * 
 */
final public class DoesNotFollowVisitedUrlVisitor implements PageVisitor {

    private final PageVisitor visitor;
    // Using map since jdk 1.5 does not provide a good concurrent set
    // implementation
    private final Map<Url, String> visitedUrls = new ConcurrentHashMap<Url, String>();

    public DoesNotFollowVisitedUrlVisitor(final String beginUrl, final PageVisitor visitor) {
        this.visitor = visitor;
        visitedUrls.put(new Url(beginUrl, 0), "");
    }

    public boolean followUrl(final Url url) {
        if (visitedUrls.get(url) != null) {
            return false;
        }
        visitedUrls.put(url, "");
        return visitor.followUrl(url);
    }

    public void onError(final Url url, final Status statusError) {
        visitor.onError(url, statusError);

    }

    public void visit(final Page page) {
        visitor.visit(page);
    }

}
