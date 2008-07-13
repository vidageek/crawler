/**
 * 
 */
package net.vidageek.crawler.component;

/**
 * @author jonasabreu
 * 
 */
public interface ComponentFactory {

	<T> T createComponent(Class<T> clazz, Object... args);
}
