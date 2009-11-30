package net.vidageek.crawler;

import junit.framework.Assert;

import org.junit.Test;

public class StatusTest {

	@Test
	public void testToCheckNotFoundIsReturned() {
		Assert.assertEquals(Status.NOT_FOUND, Status.fromHttpCode(404));
	}
}
