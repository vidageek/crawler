/**
 * 
 */
package net.vidageek.crawler.component;

import net.vidageek.crawler.Page;

/**
 * @author jonasabreu
 * 
 */
public interface Downloader {

    Page get(String url);

}
