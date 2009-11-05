/**
 * 
 */
package net.vidageek.crawler.component;

import net.vidageek.crawler.Status;

/**
 * @author jonasabreu
 * 
 */
public interface Downloader {

	String get(String url);

	Status getErrorCode();

}
