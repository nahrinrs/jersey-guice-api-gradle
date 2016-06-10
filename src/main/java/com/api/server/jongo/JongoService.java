package com.api.server.jongo;

import org.jongo.MongoCollection;

public interface JongoService {
	public MongoCollection getCollection(String name);
}
