package org.jfl110.quickstart;

import java.net.URI;
import java.util.EnumSet;

import javax.servlet.ServletContextListener;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.google.inject.servlet.GuiceFilter;

/**
 * An embedded Jetty server.
 * 
 * @author JFL110
 */
public class EmbeddedJetty {
	
	/**
	 * Builder to construct the EmbeddedJetty server.
	 *
	 * @author JFL110
	 */
	public static class EmbeddedJettyBuilder{
		
		private ServletContextListener contextListener;
		private final int port = 8080;
		private final String resourceBasePath = "src/main/webapp";
		
		private EmbeddedJettyBuilder(){}
		
		public EmbeddedJettyBuilder withContextListener(ServletContextListener contextListener){
			this.contextListener = contextListener;
			return this;
		}
		
		public EmbeddedJetty build(){
			return new EmbeddedJetty(port,resourceBasePath,contextListener);
		}
		
	}

	private final int port;
	private final String resourceBasePath;
	private final ServletContextListener contextListener;

	private Server server;

	public static EmbeddedJettyBuilder embeddedJetty() {
		return new EmbeddedJettyBuilder();
	}

	private EmbeddedJetty(int port,String resourceBasePath,ServletContextListener contextListener) {
		
		if(contextListener == null){
			throw new IllegalArgumentException("No ServletContextListener specified");
		}
		
		this.port = port;
		this.resourceBasePath = resourceBasePath;
		this.contextListener = contextListener;
	}

	
	/**
	 * Starts the server. If using as a @Rule it is not necessary to invoke this method.
	 */
	public void start() throws Exception {

		server = new Server(port);
		
		ServletContextHandler context = new ServletContextHandler();
		context.setResourceBase(resourceBasePath);       
		context.addEventListener(contextListener);
		context.addFilter(GuiceFilter.class, "/*",EnumSet.of(javax.servlet.DispatcherType.REQUEST, javax.servlet.DispatcherType.ASYNC));
		
		server.setHandler(context);
		server.start();
	}

	
	/**
	 * Stops the server. If using as a @Rule it is not necessary to invoke this method.
	 */
	public void stop() throws Exception {
		server.stop();
	}

	
	/**
	 * Gets the base URI to use for requests to this server.
	 */
	public URI getBaseUri() {
		return server.getURI();
	}
}
