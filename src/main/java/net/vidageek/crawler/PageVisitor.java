/**
 * 
 */
package net.vidageek.crawler;

/**
 * @author jonasabreu
 * 
 */
public interface PageVisitor {

	void visit(Page page);

	boolean followUrl(String url);

	void onError(String url, StatusError statusError);

}
