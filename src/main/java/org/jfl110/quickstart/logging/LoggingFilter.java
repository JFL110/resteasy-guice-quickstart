package org.jfl110.quickstart.logging;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

@Provider
public class LoggingFilter implements ContainerRequestFilter {

	private final Logger log;

	@Inject
	LoggingFilter(ILoggerFactory loggerFactory) {
		this.log = loggerFactory.getLogger(LoggingFilter.class.getSimpleName());
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		log.info(requestContext.getMethod() + " : " + requestContext.getUriInfo().getPath());
	}
}