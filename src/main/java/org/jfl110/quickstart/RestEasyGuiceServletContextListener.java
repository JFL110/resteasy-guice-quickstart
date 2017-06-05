package org.jfl110.quickstart;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;

import com.google.common.collect.FluentIterable;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;

public abstract class RestEasyGuiceServletContextListener extends GuiceServletContextListener {
	
	private final List<Module> modules;
	private Injector injector;
	
	public RestEasyGuiceServletContextListener(Module... modules){
		this.modules = FluentIterable.from(modules).toList();
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
	
	
	static class GuiceResteasySlaveListener extends GuiceResteasyBootstrapServletContextListener{
		
		private final List<Module> modules;
		private Injector injector;
		
		GuiceResteasySlaveListener(List<Module> modules){
			this.modules = modules;
		}
		
		@Override
		protected List<Module> getModules(ServletContext context) {
			return modules;
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
