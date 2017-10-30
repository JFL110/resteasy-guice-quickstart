package org.jfl110.quickstart;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class LoggingFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		System.out.println(requestContext.getMethod() + ":" + requestContext.getUriInfo().getPath());
	}
}