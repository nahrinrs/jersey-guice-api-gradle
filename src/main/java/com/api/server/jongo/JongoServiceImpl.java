package com.api.server.jongo;

import com.api.server.config.Config;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.inject.Inject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.apache.log4j.Logger;
import org.jongo.Jongo;
import org.jongo.Mapper;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.JacksonMapper;

import java.net.UnknownHostException;

public class JongoServiceImpl implements JongoService {

	private static final Logger logger = Logger.getLogger(JongoServiceImpl.class);

	private MongoClient mongo;
	private Jongo jongo;

	@Inject
	public JongoServiceImpl(@Config("mongo.url") String mongoUrl, @Config("mongo.db") String dbName) {
		try {
			JodaModule module = new JodaModule();
			Mapper mapper =  new JacksonMapper.Builder()
				.registerModule(module)
				.enable(MapperFeature.AUTO_DETECT_GETTERS)
				.build();
			this.mongo = new MongoClient(new MongoClientURI(mongoUrl));
			this.jongo = new Jongo(this.mongo.getDB(dbName), mapper);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	public MongoCollection getCollection(String name) {
		return jongo.getCollection(name);
	}
}
