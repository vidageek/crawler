package net.vidageek.crawler.page;

import java.util.ArrayList;
import java.util.List;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.Status;

/**
 * @author jonasabreu
 * 
 */
final public class ErrorPage implements Page {

    private final Status error;
    private final String url;

    public ErrorPage(final String url, final Status error) {
        this.url = url;
        this.error = error;
    }

    public String getContent() {
        return "";
    }

    public List<String> getLinks() {
        return new ArrayList<String>();
    }

    public String getUrl() {
        return url;
    }

    public Status getStatusCode() {
        return error;
    }

    public String getCharset() {
        return "";
    }

}
