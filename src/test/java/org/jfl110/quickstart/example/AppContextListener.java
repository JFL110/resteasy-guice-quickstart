package org.jfl110.quickstart.example;

import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.jfl110.quickstart.RestEasyGuiceServletContextListener;
import org.jfl110.quickstart.StringServlet;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;

/**
 * Context listener which sets up the injector when a new context is created.
 *
 * @author JFL110
 */
class AppContextListener extends RestEasyGuiceServletContextListener {

	public AppContextListener() {
		super(new Module());
	}

	/**
	 * ServletModule which defines the app bindings and endpoints.
	 *
	 * @author JFL110
	 */
	private static class Module extends ServletModule {
		@Override
		protected void configureServlets() {

			// Hook Guice and Restlet together
			bind(HttpServletDispatcher.class).in(Singleton.class);
			serve("/service/*").with(HttpServletDispatcher.class);

			// Bind Jax-RS resources
			bind(ExampleResource.class);
			bind(ExampleFilter.class);

			// Bind a regular servlet
			serve("/word").with(StringServlet.of("bird"));
		}
	}
}
