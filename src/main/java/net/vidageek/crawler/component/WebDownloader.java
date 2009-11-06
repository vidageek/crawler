/**
 * 
 */
package net.vidageek.crawler.component;

import java.io.IOException;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.Status;
import net.vidageek.crawler.exception.CrawlerException;
import net.vidageek.crawler.page.ErrorPage;
import net.vidageek.crawler.page.OkPage;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * @author jonasabreu
 * 
 */
public class WebDownloader implements Downloader {

    private final static HttpClient client = new HttpClient();

    public Page get(final String url) {
        try {

            GetMethod method = new GetMethod(url);
            Status status = Status.fromHttpCode(client.executeMethod(method));

            if (Status.OK.equals(status)) {
                return new OkPage(url, method.getResponseBodyAsString());
            }
            return new ErrorPage(url, status);

        } catch (HttpException e) {
            throw new CrawlerException("Could not retrieve data from " + url, e);
        } catch (IOException e) {
            throw new CrawlerException("Could not retrieve data from " + url, e);
        }
    }

}
