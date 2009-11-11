package net.vidageek.crawler.component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jonasabreu
 * 
 */
final public class ExecutorCounter {

    private final AtomicInteger numberOfExecutors = new AtomicInteger(0);

    public void increase() {
        numberOfExecutors.incrementAndGet();
    }

    public void decrease() {
        numberOfExecutors.decrementAndGet();
    }

    public int value() {
        return numberOfExecutors.get();
    }

}
