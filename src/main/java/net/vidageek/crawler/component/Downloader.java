/**
 * 
 */
package net.vidageek.crawler.component;

import net.vidageek.crawler.StatusError;

/**
 * @author jonasabreu
 * 
 */
public interface Downloader {

	String get(String url);

	StatusError getErrorCode();

}
