package net.vidageek.crawler.page;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.Status;

/**
 * Default implementation for {@link PageFactory}.
 */
public class DefaultPageFactory implements PageFactory {
	
	@Override
	public Page buildOkPage(String url, String content) {
		return new OkPage(url, content);
	}

	@Override
	public Page buildErrorPage(String url, Status status) {
		return new ErrorPage(url, status);
	}

	@Override
	public Page buildRejectedMimeTypePage(String url, Status status, String mimeType) {
		return new RejectedMimeTypePage(url, status, mimeType);
	}
}
