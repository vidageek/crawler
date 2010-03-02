/**
 * 
 */
package net.vidageek.crawler.page;

import java.util.ArrayList;
import java.util.List;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.Status;
import net.vidageek.crawler.link.DefaultLinkFinder;
import net.vidageek.crawler.link.FrameLinkFinder;
import net.vidageek.crawler.link.IframeLinkFinder;

/**
 * @author jonasabreu
 * 
 */
public class OkPage implements Page {

	private final String url;
	private final String content;

	public OkPage(final String url, final String content) {
		if ((url == null) || (url.trim().length() == 0)) {
			throw new IllegalArgumentException("url cannot be null");
		}
		this.url = url;
		this.content = content;
	}

	public List<String> getLinks() {

		List<String> list = new ArrayList<String>();
		list.addAll(new DefaultLinkFinder(content).getLinks());
		list.addAll(new IframeLinkFinder(content).getLinks());
		list.addAll(new FrameLinkFinder(content).getLinks());

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
		return "UTF-8";
	}
}
