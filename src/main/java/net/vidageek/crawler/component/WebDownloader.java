/**
 * 
 */
package net.vidageek.crawler.component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Scanner;

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

            String encodedUrl = encode(url);

            GetMethod method = new GetMethod(encodedUrl);
            Status status = Status.fromHttpCode(client.executeMethod(method));

            if (Status.OK.equals(status)) {
                return new OkPage(url, read(method.getResponseBodyAsStream(), method.getResponseCharSet()), method
                    .getResponseCharSet());
            }
            return new ErrorPage(url, status);

        } catch (HttpException e) {
            throw new CrawlerException("Could not retrieve data from " + url, e);
        } catch (IOException e) {
            throw new CrawlerException("Could not retrieve data from " + url, e);
        }
    }

    private String read(final InputStream inputStream, final String charset) {
        try {
            String encodedString = new Scanner(new InputStreamReader(inputStream, charset)).useDelimiter("$$").next();

            return new String(encodedString.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new CrawlerException("Encode not supported: " + charset, e);
        }
    }

    private String encode(final String url) {
        String res = "";
        for (char c : url.toCharArray()) {
            if (!":/.?#=".contains("" + c)) {
                try {
                    res += URLEncoder.encode("" + c, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new CrawlerException(
                            "There is something really wrong with your JVM. It could not find UTF-8 encoding.", e);
                }
            } else {
                res += c;
            }
        }

        return res;
    }
}
