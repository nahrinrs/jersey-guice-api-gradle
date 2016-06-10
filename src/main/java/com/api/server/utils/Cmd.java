package com.api.server.utils;

import com.google.common.base.Joiner;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

public class Cmd {

	private static final Logger logger = Logger.getLogger(Cmd.class);

	private static CommandLine commandLine;

	public static void init(String[] args){
		try {
			logger.info("Started with command line " + Joiner.on(" ").join(args));
			CommandLineParser parser = new BasicParser();
			commandLine = parser.parse(getOptions(), args);
		}catch (ParseException e){
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	public static String getWebAppPath(){
		return getOption("webapp", "src/main/webapp");
	}
	public static String getEnvFilePath(){
		return getOption("env", null);
	}
	public static String getConfigPath(){
		return getOption("config", null);
	}

	private static Options getOptions(){
		Options options = new Options();
		options.addOption("context", true, "Server Context");
		options.addOption("webapp", true, "Web App Path");
		options.addOption("env", true, "Environment");
		options.addOption("config", true, "Config Path");
		return options;
	}

	public static String getOption(String name){ return getOption(name, null); }
	public static String getOption(String name, String defaultValue){
		return (commandLine.hasOption(name) ? commandLine.getOptionValue(name) : defaultValue);
	}
}
