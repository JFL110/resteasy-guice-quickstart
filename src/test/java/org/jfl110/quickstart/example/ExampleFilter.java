package org.jfl110.quickstart.example;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.ws.rs.NameBinding;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.eclipse.jetty.server.Response;
import org.jfl110.quickstart.example.ExampleFilter.AuthRequired;

@Provider
@AuthRequired
public class ExampleFilter implements ContainerRequestFilter{
	
	@NameBinding
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.METHOD,ElementType.TYPE})
	public @interface AuthRequired {}
	
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String headerTokenValue = requestContext.getHeaderString("token");
		
		if(headerTokenValue == null){
			requestContext.abortWith(javax.ws.rs.core.Response.status(Response.SC_NOT_ACCEPTABLE).build());
		}
	}
}