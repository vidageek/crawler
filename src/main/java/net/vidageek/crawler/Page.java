/**
 * 
 */
package net.vidageek.crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jonasabreu
 * 
 */
public class Page {

	private final String url;
	private final String content;

	public Page(final String url, final Downloader downloader) {
		if (url == null || url.trim().length() == 0) {
			throw new IllegalArgumentException("url cannot be null");
		}
		if (downloader == null) {
			throw new IllegalArgumentException("downloader cannot be null");
		}
		this.url = url;
		this.content = downloader.get(this.url);
	}

	public List<String> getLinks() {
		Pattern pattern = Pattern.compile("(?i)(?s)<\\s*?a.*?href=\"(.*?)\".*?>");

		Matcher matcher = pattern.matcher(this.content);

		List<String> list = new ArrayList<String>();
		while (matcher.find()) {
			list.add(matcher.group(1));
		}
		return list;

	}

	public String getUrl() {
		return this.url;
	}

	public String getContent() {
		return this.content;
	}
}
