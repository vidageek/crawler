/**
 * 
 */
package net.vidageek.crawler;

/**
 * @author jonasabreu
 * 
 */
public interface PageVisitor {

	void visit(String page);

	boolean followUrl(String url);

}
