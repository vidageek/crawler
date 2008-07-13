/**
 * 
 */
package net.vidageek.crawler.component;

import net.vidageek.crawler.exception.CrawlerException;

/**
 * @author jonasabreu
 * 
 */
public class DefaultComponentFactory implements ComponentFactory {

	@SuppressWarnings("unchecked")
	public <T> T createComponent(final Class<T> clazz, final Object... args) {
		if (LinkNormalizer.class.equals(clazz)) {
			return (T) new DefaultLinkNormalizer((String) args[0]);
		} else if (Downloader.class.equals(clazz)) {
			return (T) new WebDownloader();
		}
		throw new CrawlerException("cannot create component for class " + clazz.getSimpleName());
	}
}
