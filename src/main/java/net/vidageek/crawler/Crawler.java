package net.vidageek.crawler;

import java.util.Map;

import org.docopt.Docopt;

import net.vidageek.crawler.component.NoAnchorLinkNormalizer;
import net.vidageek.crawler.config.CrawlerConfiguration;
import net.vidageek.crawler.config.CrawlerConfigurationBuilder;
import net.vidageek.crawler.visitor.DownloadMatchingVisitor;

public class Crawler {

	private final static String doc = String.join("\n",
			"Starts crawling a domain and saving results on output folder",
			"",
			"Usage:",
			"    start <url> [options]",
			"",
			"Options:",
			"    --valid-pages <valid-pages>  Regex that match pages to be visited [default: .*]",
			"    --download-pages <download-pages>  Regex that match pages to be downloaded [default: .*]",
			"    --output-folder <output-folder>  Folder to save pages. [default: output]",
			"",
			"Examples:",
			"    start https://www.google.com");

	public static void main(String[] args) {
		
		Map<String, Object> params = new Docopt(doc).parse(args);
		String url = (String)params.get("<url>");
		
		
		CrawlerConfiguration cfg = new CrawlerConfigurationBuilder(url).withMaxParallelRequests(20).withRequestDelay(500).withLinkNormalizer(new NoAnchorLinkNormalizer(url)).build();
		
		PageCrawler crawler = new PageCrawler(cfg);
		
		crawler.crawl(new DownloadMatchingVisitor((String)params.get("--valid-pages"),
												 (String)params.get("--download-pages"),
												 (String)params.get("--output-folder")));
	}
}
