package net.vidageek.crawler.queue;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author jonasabreu
 * 
 */
final public class DelayedBlockingQueueTest {

	@Test
	public void testThatPoolRespectsDelayTime() throws InterruptedException {
		DelayedBlockingQueue queue = new DelayedBlockingQueue(1000);
		queue.put(new FakeRunnable());
		queue.put(new FakeRunnable());

		long first = System.currentTimeMillis();
		queue.poll();
		queue.poll();

		final long last = System.currentTimeMillis() - first;

		System.out.println("First: [" + first + "] Last: [" + last + "]");
		Assert.assertTrue(last > 1000);
		Assert.assertTrue(last < 1300);
	}

	@Test
	public void testThatPoolWithArgumentsRespectsDelayTime() throws InterruptedException {
		DelayedBlockingQueue queue = new DelayedBlockingQueue(1000);
		queue.put(new FakeRunnable());
		queue.put(new FakeRunnable());

		long first = System.currentTimeMillis();
		queue.poll(100, TimeUnit.NANOSECONDS);
		queue.poll(100, TimeUnit.NANOSECONDS);

		final long last = System.currentTimeMillis() - first;

		System.out.println("First: [" + first + "] Last: [" + last + "]");
		Assert.assertTrue(last > 1000);
		Assert.assertTrue(last < 1300);
	}

	@Test
	public void testThatTakeRespectsDelayTime() throws InterruptedException {
		DelayedBlockingQueue queue = new DelayedBlockingQueue(1000);
		queue.put(new FakeRunnable());
		queue.put(new FakeRunnable());

		long first = System.currentTimeMillis();
		queue.take();
		queue.take();

		final long last = System.currentTimeMillis() - first;

		System.out.println("First: [" + first + "] Last: [" + last + "]");
		Assert.assertTrue(last > 1000);
		Assert.assertTrue(last < 1300);
	}

	@Test
	public void testThatRemoveRespectsDelayTime() throws InterruptedException {
		DelayedBlockingQueue queue = new DelayedBlockingQueue(1000);
		queue.put(new FakeRunnable());
		queue.put(new FakeRunnable());

		long first = System.currentTimeMillis();
		queue.remove();
		queue.remove();

		final long last = System.currentTimeMillis() - first;

		System.out.println("First: [" + first + "] Last: [" + last + "]");
		Assert.assertTrue(last > 1000);
		Assert.assertTrue(last < 1300);
	}

	public class FakeRunnable implements Runnable {
		public void run() {
		}
	}

}
