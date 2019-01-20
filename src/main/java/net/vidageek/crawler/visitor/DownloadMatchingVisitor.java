package net.vidageek.crawler.visitor;

import java.io.File;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.PageVisitor;
import net.vidageek.crawler.Status;
import net.vidageek.crawler.Url;

public class DownloadMatchingVisitor implements PageVisitor {

	private static final Logger log = Logger.getLogger(DownloadMatchingVisitor.class);
	private final Pattern validPages;
	private final File outputFolder;
	private final Pattern downloadPages;

	public DownloadMatchingVisitor(final String validPages, final String downloadPages, final String outputFolder) {
		this.validPages = Pattern.compile(validPages);
		this.downloadPages = Pattern.compile(downloadPages);
		this.outputFolder = new File(outputFolder);
	}

	@Override
	public void visit(final Page page) {
		log.info("Visiting page [" + page.getUrl() + "]");

		if (downloadPages.matcher(page.getUrl()).matches()) {
			log.info("Download page [" + page.getUrl() + "]");

			try (PrintWriter writer = new PrintWriter(new File(outputFolder, pageAsFile(page.getUrl())))) {
				writer.print(page.getContent());
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

	private String pageAsFile(final String url) throws UnsupportedEncodingException {
		return URLEncoder.encode(url, "UTF-8");
	}

	@Override
	public void onError(final Url errorUrl, final Status statusError) {
		log.error("Failed to download [" + errorUrl + "] with status " + statusError);

	}

	@Override
	public boolean followUrl(final Url url) {
		return validPages.matcher(url.link()).matches() && url.depth() < 4;
	}

}
