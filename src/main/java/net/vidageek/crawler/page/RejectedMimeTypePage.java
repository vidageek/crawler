package net.vidageek.crawler.page;

import java.util.ArrayList;
import java.util.List;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.Status;

/**
 * @author jonasabreu
 * 
 */
final public class RejectedMimeTypePage implements Page {

    private final String url;
    private final Status status;
    private final String mimeType;

    public RejectedMimeTypePage(final String url, final Status status, final String mimeType) {
        this.url = url;
        this.status = status;
        this.mimeType = mimeType;
    }

    public String getCharset() {
        return "";
    }

    public String getContent() {
        return "";
    }

    public List<String> getLinks() {
        return new ArrayList<String>();
    }

    public Status getStatusCode() {
        return status;
    }

    public String getUrl() {
        return url;
    }

    public String getMimeType() {
        return mimeType;
    }

}
