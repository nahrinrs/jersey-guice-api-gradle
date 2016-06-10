package com.api.server.utils;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Env {

	public static class Environment{
		public String environment;
		public String version;
		public String commit;

		public Environment() {
			version = "unknown";
			commit = "unknown";
			environment = "unknown";
		}
	}

	private static final Logger logger = Logger.getLogger(Env.class);
	private static Environment environment;

	public static void init() {
		try {
			String envFilePath = Cmd.getEnvFilePath();
			if ((null != envFilePath) && !envFilePath.isEmpty()) {
				InputStream envContent = new FileInputStream(envFilePath);
				if(envContent != null){
					environment = JSON.decode(envContent, Environment.class);
					logger.info("Environment " + environment.environment);
					logger.info("Version " + environment.version);
					logger.info("Commit " + environment.commit);
				}else{
					throw new FileNotFoundException(Cmd.getEnvFilePath());
				}
			}
		} catch (Throwable e) {
			logger.warn("Environment File Reading Error", e);
		}

		if (null == environment) {
			environment = new Environment();
		}
	}

	public static String env(){
		String env = System.getenv("API_ENV");
		if(env==null){
			env = Env.getEnvironment().environment;
		}
		return env;
	}

	public static String path(){
		String env = System.getenv("API_CONFIG");
		if(env==null){
			env = Cmd.getConfigPath();
		}
		return env;
	}

	public static boolean isLocal(){
		String env = System.getenv("API_LOCAL");
		return (env != null && env.equals("1"));
	}

	public static Environment getEnvironment(){
		return environment;
	}
}
