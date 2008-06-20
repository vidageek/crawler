/**
 * 
 */
package net.vidageek.crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.vidageek.crawler.exception.CrawlerException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * @author jonasabreu
 * 
 */
public class PageCrawler {

	private final String beginUrl;

	private static final HttpClient client = new HttpClient();

	private static final Set<String> visitedUrls = new HashSet<String>();

	public PageCrawler(final String beginUrl) {
		if (beginUrl == null) {
			throw new IllegalArgumentException("beginUrl cannot be null");
		}
		if (beginUrl.trim().length() == 0) {
			throw new IllegalArgumentException("beginUrl cannot be empty");
		}
		if (!beginUrl.startsWith("http://")) {
			throw new IllegalArgumentException("beginUrl must start with http://");
		}
		this.beginUrl = beginUrl;

	}

	public void craw(final PageVisitor visitor) {
		if (visitor == null) {
			throw new NullPointerException("visitor cannot be null");
		}

		if (!visitedUrls.contains(this.beginUrl)) {
			visitedUrls.add(this.beginUrl);
			String page = this.downloadPage(this.beginUrl);
			visitor.visit(page);
			List<String> links = this.recoverUrls(page);
			for (String link : links) {
				new PageCrawler(link).craw(visitor);
			}

		}

	}

	private List<String> recoverUrls(final String page) {
		Pattern pattern = Pattern.compile("(?i)(?m)<\\s*?a.*?href=\"(.*?)\".*?>");
		Matcher matcher = pattern.matcher(page);
		List<String> list = new ArrayList<String>();
		while (matcher.find()) {
			list.add(matcher.group(1));
		}
		return list;
	}

	private String downloadPage(final String url) {
		try {
			GetMethod method = new GetMethod(url);
			client.executeMethod(method);
			return method.getResponseBodyAsString();
		} catch (HttpException e) {
			throw new CrawlerException("Error retrieving data from URL " + url);
		} catch (IOException e) {
			throw new CrawlerException("Error retrieving data from URL " + url);
		}
	}
}
