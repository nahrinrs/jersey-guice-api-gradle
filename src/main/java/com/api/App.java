package com.api;

import java.util.EnumSet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.api.server.ServerModule;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceFilter;

import org.apache.log4j.Logger;

public class App extends AbstractModule {

	static final Logger logger = Logger.getLogger(App.class);

	public static void main(String[] args) {
		logger.info("<<<<<<<<<<<<<<<<<<< APP STARTED >>>>>>>>>>>>>>>>>>>>");
		Guice.createInjector(Stage.PRODUCTION, new App());

		int port = 5000;
		Server server = new Server(port);

		ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
		context.addFilter(GuiceFilter.class, "/*", EnumSet.<javax.servlet
				.DispatcherType> of(javax.servlet.DispatcherType.REQUEST, javax.servlet.DispatcherType.ASYNC));
		context.addServlet(DefaultServlet.class, "/*");

		try {
			server.start();
			server.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void configure() {
		install(new ServerModule());

	}

}
