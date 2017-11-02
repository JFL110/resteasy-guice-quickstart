package org.jfl110.quickstart.example;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jfl110.quickstart.example.ExampleFilter.AuthRequired;
import org.jfl110.quickstart.example.ExampleService.JsonBean;
import org.jfl110.quickstart.time.CurrentDateTime;
import org.joda.time.LocalDateTime;

import com.google.inject.Provider;

@Singleton
@Path("/service/resource")
public class ExampleResource {

	private final ExampleService exampleService;
	private final Provider<LocalDateTime> currentTime;

	@Inject
	ExampleResource(ExampleService exampleService, @CurrentDateTime Provider<LocalDateTime> currentTime) {
		this.exampleService = exampleService;
		this.currentTime = currentTime;
	}

	@AuthRequired
	@GET
	@Path("/secure-bean")
	@Produces(MediaType.APPLICATION_JSON)
	public JsonBean readSecureBean(@QueryParam("name") String name) {
		return exampleService.getSecureBean(name);
	}

	@GET
	@Path("/read-bean")
	@Produces(MediaType.APPLICATION_JSON)
	public JsonBean readBean() {
		return exampleService.readBean();
	}

	@GET
	@Path("/time")
	@Produces(MediaType.TEXT_PLAIN)
	public String getTime() {
		return currentTime.get().toString();
	}
}