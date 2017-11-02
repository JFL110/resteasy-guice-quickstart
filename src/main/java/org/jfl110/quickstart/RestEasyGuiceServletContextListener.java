package org.jfl110.quickstart;

import java.util.Collection;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;
import org.jfl110.quickstart.logging.Log4JLoggerModule;
import org.jfl110.quickstart.time.CurrentDateTimeModule;

import com.google.common.collect.ImmutableList;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.util.Modules;

/**
 * GuiceServletContextListener that hooks Guice with Resteasy.
 *
 * @author JFL110
 */
public abstract class RestEasyGuiceServletContextListener extends GuiceServletContextListener {

	private final List<Module> modules;
	private Injector injector;

	public RestEasyGuiceServletContextListener(Module... modules) {
		this.modules = ImmutableList.copyOf(modules);
	}

	public RestEasyGuiceServletContextListener(Collection<Module> modules) {
		this.modules = ImmutableList.copyOf(modules);
	}

	@Override
	protected Injector getInjector() {
		return injector;
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		GuiceResteasySlaveListener resteasyListener = new GuiceResteasySlaveListener(modules);
		resteasyListener.contextInitialized(servletContextEvent);

		injector = resteasyListener.getInjector();

		super.contextInitialized(servletContextEvent);
	}

	private static final class GuiceResteasySlaveListener extends GuiceResteasyBootstrapServletContextListener {

		private final ImmutableList<Module> defaultModules;
		private final List<Module> modules;
		private Injector injector;

		GuiceResteasySlaveListener(List<Module> modules) {
			this.modules = modules;
			this.defaultModules = ImmutableList.of(new CurrentDateTimeModule(), new Log4JLoggerModule());
		}

		@Override
		protected List<Module> getModules(ServletContext context) {
			return ImmutableList.of(Modules.override(defaultModules).with(modules));
		}

		@Override
		protected void withInjector(Injector injector) {
			super.withInjector(injector);
			this.injector = injector;
		}

		Injector getInjector() {
			return injector;
		}
	}
}
