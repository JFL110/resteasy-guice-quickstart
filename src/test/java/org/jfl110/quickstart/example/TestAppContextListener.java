package org.jfl110.quickstart.example;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.jfl110.quickstart.RestEasyGuiceServletContextListener;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;

/**
 * Context listener which sets up the injector when a new context is created.
 *
 * @author JFL110
 */
class TestAppContextListener extends RestEasyGuiceServletContextListener {

	public TestAppContextListener() {
		super(new Module());
	}
	
	/**
	 * ServletModule which defines the app bindings and endpoints.
	 *
	 * @author JFL110
	 */
	private static class Module extends ServletModule
	{
		@SuppressWarnings("serial")
		@Override
		protected void configureServlets() {

			// Hook Guice and Restlet together
			bind(HttpServletDispatcher.class).in(Singleton.class);
			serve("/service/*").with(HttpServletDispatcher.class);

			// Bind Jax-RS resources
			bind(ExampleResource.class);
			bind(ExampleFilter.class);

			// Bind a regular servlet
			serve("/word").with(new HttpServlet() {
				@Override
				protected void doGet(HttpServletRequest req, HttpServletResponse resp)
						throws ServletException, IOException {
					resp.getWriter().print("bird");
				}
			});
		}
	}
}
