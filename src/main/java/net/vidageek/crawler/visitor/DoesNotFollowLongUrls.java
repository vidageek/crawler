package net.vidageek.crawler.visitor;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.PageVisitor;
import net.vidageek.crawler.Status;

/**
 * @author jonasabreu
 * 
 */
final public class DoesNotFollowLongUrls implements PageVisitor {

    private final PageVisitor visitor;

    public DoesNotFollowLongUrls(final PageVisitor visitor) {
        this.visitor = visitor;
    }

    public boolean followUrl(final String url) {
        if (url.length() > 150) {
            return false;
        }
        return visitor.followUrl(url);
    }

    public void onError(final String url, final Status statusError) {
        visitor.onError(url, statusError);
    }

    public void visit(final Page page) {
        visitor.visit(page);
    }

}
