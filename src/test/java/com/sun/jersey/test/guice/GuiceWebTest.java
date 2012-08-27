/*
 * Jersey Guice Module Copyright (C) 2012 Juha Heljoranta
 *
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by the 
 * Free Software Foundation; either version 2.1 of the License, or (at your 
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License 
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this library; if not, write to the Free Software Foundation, 
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.sun.jersey.test.guice;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.junit.Assert;
import org.junit.Test;

import com.google.inject.Binder;
import com.sun.jersey.api.client.WebResource;

public class GuiceWebTest extends GuiceJerseyTest {

	public static class MyModule extends JerseyModule {

		@Override
		protected void configure(Binder b) {
			b.bind(TestService.class);
			b.bind(TestResource.class);
		}

	}

	@Path("root")
	public static class TestResource {

		private com.sun.jersey.test.guice.GuiceWebTest.TestService service;

		@Inject
		public TestResource(TestService service) {
			this.service = service;
		}

		@GET
		public String get() {
			return "Hello, " + service.say();
		}

	}

	public static class TestService {
		public String say() {
			return "World!";
		}
	}

	public GuiceWebTest() {
		super(MyModule.class);
	}

	@Test
	public void testGet() {
		WebResource r = resource().path("root");
		String s = r.get(String.class);
		Assert.assertEquals("Hello, World!", s);
	}

}
