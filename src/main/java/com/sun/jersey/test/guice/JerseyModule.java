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

import static com.google.inject.Guice.createInjector;

import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;

import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/**
 * Template class to configure bindings.
 * <p>
 * Common usage pattern:
 * 
 * <pre>
 * class MyTest extends GuiceJerseyTest {
 * 	class MyModule extends JerseyModule {
 * 		protected void configure(Binder b) {
 * 			b.bind(FooService.class);
 * 			b.bind(BarResource.class);
 * 		}
 * 	}
 * 
 * 	public MyTest() {
 * 		super(MyModule.class);
 * 	}
 * }
 * </pre>
 * <p>
 * This class is not thread safe: Tests must be executed by the same thread
 * which instantiated the {@link JerseyModule} class.
 * 
 * @author Juha Heljoranta
 * 
 */
public abstract class JerseyModule extends GuiceServletContextListener {

	private static final ThreadLocal<Injector> threadLocalInj = new ThreadLocal<Injector>();

	static Injector injector() {
		return threadLocalInj.get();
	}

	private final Logger logger = Logger
			.getLogger(JerseyModule.class.getName());

	protected abstract void configure(Binder binder);

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		super.contextDestroyed(servletContextEvent);
		threadLocalInj.remove();
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		if (threadLocalInj.get() != null) {
			logger.warning("Injector already exists. ServletContextListener.contextDestroyed was not invoked?");
		}
		super.contextInitialized(servletContextEvent);
		Injector in = (Injector) servletContextEvent.getServletContext()
				.getAttribute(Injector.class.getName());
		if (in == null) {
			logger.warning("Injector is not set");
		}
		threadLocalInj.set(in);
	}

	@Override
	protected Injector getInjector() {
		return createInjector(makeJerseyServletModule());
	}

	protected JerseyServletModule makeJerseyServletModule() {
		return new JerseyServletModule() {

			@Override
			protected void configureServlets() {
				JerseyModule.this.configure(binder());
				serve("/*").with(GuiceContainer.class);
			}

		};
	}

}
