package org.jfl110.quickstart.logging;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;

/**
 * Module that enables injecting the ILoggerFactory to get a org.slf4j.Logger
 *
 * @author JFL110
 */
public final class Log4JLoggerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ILoggerFactory.class).toInstance(new ILoggerFactory() {
			@Override
			public Logger getLogger(String name) {
				return LoggerFactory.getLogger(name);
			}
		});
	}
}