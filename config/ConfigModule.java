package com.dcoc.unravel.server.config;

import com.dcoc.unravel.server.utils.Env;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

public class ConfigModule extends AbstractModule {

	private static final Logger logger = Logger.getLogger(ConfigModule.class);

	@Override
	protected void configure() {
		String env  = Env.env();
		String path = Env.path();
		logger.info("ENVIRONMENT " + env);
		logger.info("CONFIG " + path);
		ConfigLoader loader = new ConfigLoader("resource:com/dcoc/unravel/server/config/config.json");
		loader.load();
		if(!env.equals("unknown")){
			loader.patch("resource:com/dcoc/unravel/server/config/"+env+".json");
		}
		if (path != null) {
			loader.patch("file:"+path);
		}
		Map<String,Object> config = loader.flat();
		config.put("env",env);
		config.put("path",path);
		for (Map.Entry<String,Object> entry:config.entrySet()) {
			String key = entry.getKey();
			Object val = entry.getValue();
			if (val != null) {
				Class<Object> cls = (Class<Object>) val.getClass();
				if (List.class.isAssignableFrom(cls)) {
					bind(List.class)
						.annotatedWith(new ConfigImpl(key))
						.toInstance((List) val);
				}else if(Map.class.isAssignableFrom(cls)) {
					bind(Map.class)
						.annotatedWith(new ConfigImpl(key))
						.toInstance((Map) val);
				} else {
					bind(cls)
						.annotatedWith(new ConfigImpl(key))
						.toInstance(val);
				}
				Names.named("");
			}
		}
		bind(Map.class)
			.annotatedWith(new ConfigImpl("CONFIG"))
			.toInstance(config);
	}
}
