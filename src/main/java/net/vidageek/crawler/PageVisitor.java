/**
 * 
 */
package net.vidageek.crawler;

/**
 * @author jonasabreu
 * 
 */
public interface PageVisitor extends ContentVisitor {

    boolean followUrl(Url url);

}
