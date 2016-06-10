package com.api.server.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.Map;

public class JSON {

	private static final Logger logger = Logger.getLogger(JSON.class);
	private static final ObjectMapper mapper = new ObjectMapper();

	public static String encode(Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String prettyPrint(Object obj) {
		try {
			return mapper
				.writerWithDefaultPrettyPrinter()
				.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T> T decode(String json, Class<T> clazz) {
		try {
			return mapper.readValue(json, clazz);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ex.printStackTrace();
			return null;
		}
	}

	public static <T> T decode(InputStream stream, Class<T> clazz) {
		try {
			return mapper.readValue(stream, clazz);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ex.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> toMap(String jsonString) {
		return decode(jsonString, Map.class);
	}
}
