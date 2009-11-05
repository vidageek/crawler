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
            int methodStatus = client.executeMethod(method);

            if ((methodStatus >= 400) && (methodStatus <= 499)) {
                return new ErrorPage(url, Status.NOT_FOUND);
            }
            if ((methodStatus >= 500) && (methodStatus <= 599)) {
                return new ErrorPage(url, Status.UNAUTHORIZED);
            }
            if ((methodStatus < 200) || (methodStatus > 299)) {
                throw new CrawlerException("Could not retrieve data from " + url + ". Error code: " + methodStatus);
            }

            return new OkPage(url, method.getResponseBodyAsString());

        } catch (HttpException e) {
            throw new CrawlerException("Could not retrieve data from " + url, e);
        } catch (IOException e) {
            throw new CrawlerException("Could not retrieve data from " + url, e);
        }
    }

}
