package org.jfl110.quickstart.time;

import org.joda.time.LocalDateTime;

import com.google.inject.AbstractModule;

/**
 * Module that binds any org.joda.time.LocalDateTime annotated
 * with @CurrentDateTime to the current time.
 *
 * @author JFL110
 */
public final class CurrentDateTimeModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(LocalDateTime.class).annotatedWith(CurrentDateTime.class).toProvider(() -> LocalDateTime.now());
	}
}