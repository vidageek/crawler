package net.vidageek.crawler.page;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.Status;

/**
 * Contract for {@link net.vidageek.crawler.Page}s factory.
 */
public interface PageFactory {
	
	Page buildOkPage(String url, String content);

	Page buildErrorPage(String url, Status status);

	Page buildRejectedMimeTypePage(String url, Status status, String mimeType);
}
