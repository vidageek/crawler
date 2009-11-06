/**
 * 
 */
package net.vidageek.crawler.page;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.Status;

/**
 * @author jonasabreu
 * 
 */
public class OkPage implements Page {

    private final String url;
    private final String content;
    private final String charset;

    public OkPage(final String url, final String content, final String charset) {
        if ((url == null) || (url.trim().length() == 0)) {
            throw new IllegalArgumentException("url cannot be null");
        }
        this.url = url;
        this.content = content;
        this.charset = charset;
    }

    public List<String> getLinks() {
        Pattern pattern = Pattern.compile("(?i)(?s)<\\s*?a.*?href=\"(.*?)\".*?>");

        Matcher matcher = pattern.matcher(content);

        List<String> list = new ArrayList<String>();
        while (matcher.find()) {
            list.add(matcher.group(1));
        }
        return list;

    }

    public String getUrl() {
        return url;
    }

    public String getContent() {
        return content;
    }

    public Status getStatusCode() {
        return Status.OK;
    }

    public String getCharset() {
        return charset;
    }
}
