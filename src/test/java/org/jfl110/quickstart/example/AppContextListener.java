package org.jfl110.quickstart.example;

import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.jfl110.quickstart.FileServlet;
import org.jfl110.quickstart.RestEasyGuiceServletContextListener;
import org.jfl110.quickstart.StringServlet;
import org.jfl110.quickstart.logging.LoggingFilter;

import com.google.common.collect.FluentIterable;
import com.google.inject.Module;
import com.google.inject.servlet.ServletModule;

/**
 * Context listener which sets up the injector when a new context is created.
 *
 * @author JFL110
 */
class AppContextListener extends RestEasyGuiceServletContextListener {

	AppContextListener(Module... overrideModules) {
		super(FluentIterable.from(overrideModules).append(new AppModule()).toList());
	}

	/**
	 * ServletModule which defines the app bindings and endpoints.
	 *
	 * @author JFL110
	 */
	private static class AppModule extends ServletModule {
		@Override
		protected void configureServlets() {

			// Hook Guice and Restlet together
			serve("/service/*").with(HttpServletDispatcher.class);

			// Bind Jax-RS resources
			bind(ExampleResource.class);
			bind(ExampleFilter.class);
			bind(LoggingFilter.class);

			// Bind a regular servlet
			serve("/word").with(StringServlet.of("bird"));
			serve("/file").with(FileServlet.ofUncachedFile("some-text.txt"));
			serve("/file-cached").with(FileServlet.ofCachedFile("some-text.txt"));
		}
	}
}