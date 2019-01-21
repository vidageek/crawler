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

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.ContentEncodingHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.log4j.Logger;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.Status;
import net.vidageek.crawler.config.http.Cookie;
import net.vidageek.crawler.exception.CrawlerException;
import net.vidageek.crawler.page.DefaultPageFactory;
import net.vidageek.crawler.page.PageFactory;

/**
 * @author jonasabreu
 *
 */
public class WebDownloader implements Downloader {

	private final ConcurrentLinkedQueue<String> mimeTypesToInclude;

	private final Logger log = Logger.getLogger(WebDownloader.class);

	private final ConcurrentLinkedQueue<Cookie> cookies;

	private final PageFactory pageFactory;

	public WebDownloader(final List<String> mimeTypesToInclude) {
		this(mimeTypesToInclude, new ArrayList<Cookie>(), new DefaultPageFactory());
	}

	public WebDownloader(final List<String> mimeTypesToInclude, final List<Cookie> cookies,
			final PageFactory pageFactory) {

		this.cookies = new ConcurrentLinkedQueue<Cookie>(cookies);
		this.mimeTypesToInclude = new ConcurrentLinkedQueue<String>(mimeTypesToInclude);
		this.pageFactory = pageFactory;
	}

	public WebDownloader() {
		this(Arrays.asList("text/html"));
	}

	@Override
	public Page get(final String url) {
		final DefaultHttpClient client = new ContentEncodingHttpClient();
		for (final Cookie cookie : cookies) {
			final String name = cookie.name();
			final String value = cookie.value();
			log.debug("Creating cookie [" + name + " = " + value + "] " + cookie.domain());
			final BasicClientCookie clientCookie = new BasicClientCookie(name, value);
			clientCookie.setPath(cookie.path());
			clientCookie.setDomain(cookie.domain());
			client.getCookieStore().addCookie(clientCookie);
		}
		client.getParams().setIntParameter("http.socket.timeout", 15000);
		return get(client, url);
	}

	public Page get(final HttpClient client, final String url) {
		try {
			final String encodedUrl = encode(url);
			log.debug("Requesting url: [" + encodedUrl + "]");
			final HttpGet method = new HttpGet(encodedUrl);

			try {
				final HttpResponse response = client.execute(method);
				final Status status = Status.fromHttpCode(response.getStatusLine().getStatusCode());

				if (!acceptsMimeType(response.getLastHeader("Content-Type"))) {
					return pageFactory.buildRejectedMimeTypePage(url, status,
							response.getLastHeader("Content-Type").getValue());
				}

				if (Status.OK.equals(status)) {
					final byte[] text = read(response.getEntity().getContent());

					final CharsetDetector detector = new CharsetDetector();
					detector.setText(text);
					final CharsetMatch match = detector.detect();

					log.debug("Detected charset: " + match.getName());

					String content;
					try {
						content = match.getString();
					} catch (final UnsupportedEncodingException e) {
						log.debug("Unsupported charset [" + match.getName() + "]. Using UTF-8.");
						content = new String(text, "UTF-8");
					}
					final CharBuffer buffer = CharBuffer.wrap(content.toCharArray());
					final Charset utf8Charset = Charset.forName("UTF-8");
					final String utf8Content = new String(utf8Charset.encode(buffer).array(), "UTF-8");

					return pageFactory.buildOkPage(url, utf8Content);
				}
				return pageFactory.buildErrorPage(url, status);
			} finally {
				method.abort();
			}

		} catch (final IOException e) {
			throw new CrawlerException("Could not retrieve data from " + url, e);
		}
	}

	private boolean acceptsMimeType(final Header header) {
		final String value = header.getValue();
		if (value == null) {
			return false;
		}

		for (final String mimeType : mimeTypesToInclude) {
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
					final byte[] newBytes = new byte[bytes.length * 3 / 2 + 1];
					for (int j = 0; j < bytes.length; j++) {
						newBytes[j] = bytes[j];
					}
					bytes = newBytes;
				}
			}
		} catch (final IOException e) {
			throw new CrawlerException("There was a problem reading stream.", e);
		}

		final byte[] copy = Arrays.copyOf(bytes, i);

		return copy;
	}

	private String encode(final String url) {
		String res = "";
		for (final char c : url.toCharArray()) {
			if (!":/.?&#=".contains("" + c)) {
				try {
					res += URLEncoder.encode("" + c, "UTF-8");
				} catch (final UnsupportedEncodingException e) {
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