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

import org.junit.Before;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

/**
 * Guice injection support for {@link JerseyTest}.
 * 
 * @see JerseyModule
 * @author Juha Heljoranta
 */
public abstract class GuiceJerseyTest extends JerseyTest {

	public GuiceJerseyTest(Class<? extends JerseyModule> guiceConfClass) {
		super(new WebAppDescriptor.Builder().filterClass(GuiceFilter.class)
				.contextListenerClass(guiceConfClass).build());
	}

	protected Injector injector;

	@Before
	public void inject() {
		injector = JerseyModule.injector();
		injector.injectMembers(this);
	}

}
