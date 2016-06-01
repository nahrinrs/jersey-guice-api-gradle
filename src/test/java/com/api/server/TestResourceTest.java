package com.api.server;

import static org.junit.Assert.*;

import org.junit.Test;

import com.api.bean.ComplexObject;
import com.api.server.resource.*;

public class TestResourceTest {

	@Test
	public void testStringEcho() {
		assertEquals("test", new TestResource().echo("test"));
	}

	@Test
	public void testObjectEcho() {
		ComplexObject co = new ComplexObject();
		co.setKey("key");
		co.setValue(123);
		assertEquals(co, new TestResource().echo(co));
	}

}
