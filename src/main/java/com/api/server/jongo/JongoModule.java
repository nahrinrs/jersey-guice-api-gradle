package com.api.server.jongo;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class JongoModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(JongoService.class).to(JongoServiceImpl.class).in(Singleton.class);
	}
}
