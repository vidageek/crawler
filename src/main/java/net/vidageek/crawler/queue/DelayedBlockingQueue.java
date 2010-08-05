package net.vidageek.crawler.queue;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author jonasabreu
 * 
 */
final public class DelayedBlockingQueue implements BlockingQueue<Runnable> {

	private final BlockingQueue<Runnable> queue;
	private final int delayInMilliseconds;
	private volatile long lastSuccesfullPop;

	public DelayedBlockingQueue(final int delayInMilliseconds) {
		this.delayInMilliseconds = delayInMilliseconds;
		queue = new LinkedBlockingQueue<Runnable>();
		lastSuccesfullPop = System.currentTimeMillis() - delayInMilliseconds;
	}

	public Runnable poll() {
		synchronized (queue) {
			while ((System.currentTimeMillis() - lastSuccesfullPop <= delayInMilliseconds) && !queue.isEmpty()) {
				sleep();
			}
			lastSuccesfullPop = System.currentTimeMillis();
			return queue.poll();
		}
	}

	public Runnable poll(final long timeout, final TimeUnit unit) throws InterruptedException {
		synchronized (queue) {
			while ((System.currentTimeMillis() - lastSuccesfullPop <= delayInMilliseconds) && !queue.isEmpty()) {
				sleep();
			}
			lastSuccesfullPop = System.currentTimeMillis();
			return queue.poll(timeout, unit);
		}
	}

	public Runnable take() throws InterruptedException {
		synchronized (queue) {
			while ((System.currentTimeMillis() - lastSuccesfullPop <= delayInMilliseconds) && !queue.isEmpty()) {
				sleep();
			}
			lastSuccesfullPop = System.currentTimeMillis();
			return queue.take();
		}
	}

	public Runnable remove() {
		synchronized (queue) {
			while ((System.currentTimeMillis() - lastSuccesfullPop <= delayInMilliseconds) && !queue.isEmpty()) {
				sleep();
			}
			lastSuccesfullPop = System.currentTimeMillis();
			return queue.remove();
		}
	}

	private void sleep() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Delegate Methods. Java is just soooo fun sometimes...

	public boolean add(final Runnable e) {
		return queue.add(e);
	}

	public boolean addAll(final Collection<? extends Runnable> c) {
		return queue.addAll(c);
	}

	public void clear() {
		queue.clear();
	}

	public boolean contains(final Object o) {
		return queue.contains(o);
	}

	public boolean containsAll(final Collection<?> c) {
		return queue.containsAll(c);
	}

	public int drainTo(final Collection<? super Runnable> c, final int maxElements) {
		return queue.drainTo(c, maxElements);
	}

	public int drainTo(final Collection<? super Runnable> c) {
		return queue.drainTo(c);
	}

	public Runnable element() {
		return queue.element();
	}

	@Override
	public boolean equals(final Object o) {
		return queue.equals(o);
	}

	@Override
	public int hashCode() {
		return queue.hashCode();
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}

	public Iterator<Runnable> iterator() {
		return queue.iterator();
	}

	public boolean offer(final Runnable e, final long timeout, final TimeUnit unit) throws InterruptedException {
		return queue.offer(e, timeout, unit);
	}

	public boolean offer(final Runnable e) {
		return queue.offer(e);
	}

	public Runnable peek() {
		return queue.peek();
	}

	public void put(final Runnable e) throws InterruptedException {
		queue.put(e);
	}

	public int remainingCapacity() {
		return queue.remainingCapacity();
	}

	public boolean remove(final Object o) {
		return queue.remove(o);
	}

	public boolean removeAll(final Collection<?> c) {
		return queue.removeAll(c);
	}

	public boolean retainAll(final Collection<?> c) {
		return queue.retainAll(c);
	}

	public int size() {
		return queue.size();
	}

	public Object[] toArray() {
		return queue.toArray();
	}

	public <T> T[] toArray(final T[] a) {
		return queue.toArray(a);
	}

}
