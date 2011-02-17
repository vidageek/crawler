/**
 * 
 */
package net.vidageek.crawler.component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.Status;
import net.vidageek.crawler.config.http.Cookie;
import net.vidageek.crawler.exception.CrawlerException;
import net.vidageek.crawler.page.ErrorPage;
import net.vidageek.crawler.page.OkPage;
import net.vidageek.crawler.page.RejectedMimeTypePage;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.log4j.Logger;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

/**
 * @author jonasabreu
 * 
 */
public class WebDownloader implements Downloader {

	private final ConcurrentLinkedQueue<String> mimeTypesToInclude;

	private final Logger log = Logger.getLogger(WebDownloader.class);

	private final ConcurrentLinkedQueue<Cookie> cookies;

	public WebDownloader(final List<String> mimeTypesToInclude) {
		this(mimeTypesToInclude, new ArrayList<Cookie>());
	}

	public WebDownloader(final List<String> mimeTypesToInclude, final List<Cookie> cookies) {
		this.cookies = new ConcurrentLinkedQueue<Cookie>(cookies);
		this.mimeTypesToInclude = new ConcurrentLinkedQueue<String>(mimeTypesToInclude);
	}

	public WebDownloader() {
		this(Arrays.asList("text/html"));
	}

	public Page get(final String url) {
		DefaultHttpClient client = new DefaultHttpClient();
		for (Cookie cookie : cookies) {
			String name = cookie.name();
			String value = cookie.value();
			log.debug("Creating cookie [" + name + " = " + value + "] " + cookie.domain());
			BasicClientCookie clientCookie = new BasicClientCookie(name, value);
			clientCookie.setPath(cookie.path());
			clientCookie.setDomain(cookie.domain());
			client.getCookieStore().addCookie(clientCookie);
		}
		client.getParams().setIntParameter("http.socket.timeout", 15000);
		return get(client, url);
	}

	public Page get(final HttpClient client, final String url) {
		try {
			String encodedUrl = encode(url);
			log.debug("Requesting url: [" + encodedUrl + "]");
			HttpGet method = new HttpGet(encodedUrl);

			try {
				HttpResponse response = client.execute(method);
				Status status = Status.fromHttpCode(response.getStatusLine().getStatusCode());

				if (!acceptsMimeType(response.getLastHeader("Content-Type"))) {
					return new RejectedMimeTypePage(url, status, response.getLastHeader("Content-Type").getValue());
				}

				if (Status.OK.equals(status)) {
					CharsetDetector detector = new CharsetDetector();
					detector.setText(read(response.getEntity().getContent()));
					CharsetMatch match = detector.detect();

					log.debug("Detected charset: " + match.getName());

					String content = match.getString();
					CharBuffer buffer = CharBuffer.wrap(content.toCharArray());
					Charset utf8Charset = Charset.forName("UTF-8");
					String utf8Content = new String(utf8Charset.encode(buffer).array(), "UTF-8");

					return new OkPage(url, utf8Content);
				}
				return new ErrorPage(url, status);
			} finally {
				method.abort();
			}

		} catch (IOException e) {
			throw new CrawlerException("Could not retrieve data from " + url, e);
		}
	}

	private boolean acceptsMimeType(final Header header) {
		final String value = header.getValue();
		if (value == null) {
			return false;
		}

		for (String mimeType : mimeTypesToInclude) {
			if (value.contains(mimeType)) {
				return true;
			}
		}
		return false;
	}

	private byte[] read(final InputStream inputStream) {
		byte[] bytes = new byte[1000];
		int i = 0;
		int b;
		try {
			while ((b = inputStream.read()) != -1) {
				bytes[i++] = (byte) b;
				if (bytes.length == i) {
					byte[] newBytes = new byte[(bytes.length * 3) / 2 + 1];
					for (int j = 0; j < bytes.length; j++) {
						newBytes[j] = bytes[j];
					}
					bytes = newBytes;
				}
			}
		} catch (IOException e) {
			new CrawlerException("There was a problem reading stream.", e);
		}

		byte[] copy = Arrays.copyOf(bytes, i);

		return copy;
	}

	private String encode(final String url) {
		String res = "";
		for (char c : url.toCharArray()) {
			if (!":/.?&#=".contains("" + c)) {
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