/**
 * 
 */
package net.vidageek.crawler.visitor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.vidageek.crawler.ContentVisitor;
import net.vidageek.crawler.Page;
import net.vidageek.crawler.PageVisitor;
import net.vidageek.crawler.Status;
import net.vidageek.crawler.Url;

/**
 * Simple visitor to crawl a single domain.
 * 
 * @author jonasabreu
 * 
 */
public class DomainVisitor implements PageVisitor {

    private final String domain;
    private final ContentVisitor visitor;

    public DomainVisitor(final String baseUrl, final ContentVisitor visitor) {
        this.visitor = visitor;
        if ((baseUrl == null) || (baseUrl.trim().length() == 0)) {
            throw new IllegalArgumentException("baseUrl cannot be null or empty");
        }
        Matcher matcher = Pattern.compile("(http://[^/]+)").matcher(baseUrl);
        if (!matcher.find()) {
            throw new IllegalArgumentException("baseUrl must match http://[^/]+");
        }
        domain = matcher.group(1) + "/";
    }

    public boolean followUrl(final Url url) {
        if (url == null) {
            return false;
        }
        return url.link().startsWith(domain);
    }

    // Delegate methods

    public void onError(final Url url, final Status statusError) {
        visitor.onError(url, statusError);
    }

    public void visit(final Page page) {
        visitor.visit(page);
    }

}
