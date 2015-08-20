package com.api.server;

import java.util.HashMap;
import java.util.Map;
import java.util.HashMap;

import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.eclipse.jetty.servlet.DefaultServlet;

import com.api.server.resource.TestResource;
import com.google.inject.Singleton;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class ServerModule extends JerseyServletModule {
	@Override
	protected void configureServlets() {
		
		bind(DefaultServlet.class).in(Singleton.class);
        bind(TestResource.class).in(Singleton.class);

        bind(MessageBodyReader.class).to(JacksonJsonProvider.class);
        bind(MessageBodyWriter.class).to(JacksonJsonProvider.class);

        Map<String, String> options = new HashMap<String, String>();
        options.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
        serve("/*").with(GuiceContainer.class, options);
	}

	
}
