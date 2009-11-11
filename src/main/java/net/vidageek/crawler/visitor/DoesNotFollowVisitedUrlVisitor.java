package net.vidageek.crawler.visitor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.PageVisitor;
import net.vidageek.crawler.Status;

/**
 * @author jonasabreu
 * 
 */
final public class DoesNotFollowVisitedUrlVisitor implements PageVisitor {

    private final PageVisitor visitor;
    // Using map since jdk 1.5 does not provide a good concurrent set
    // implementation
    private final Map<String, String> visitedUrls = new ConcurrentHashMap<String, String>();

    public DoesNotFollowVisitedUrlVisitor(final PageVisitor visitor) {
        this.visitor = visitor;
    }

    public boolean followUrl(final String url) {
        if (visitedUrls.get(url) != null) {
            return false;
        }
        return visitor.followUrl(url);
    }

    public void onError(final String url, final Status statusError) {
        visitedUrls.put(url, "");
        visitor.onError(url, statusError);

    }

    public void visit(final Page page) {
        visitedUrls.put(page.getUrl(), "");
        visitor.visit(page);
    }

}
