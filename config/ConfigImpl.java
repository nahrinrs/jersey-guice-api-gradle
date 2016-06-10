package com.dcoc.unravel.server.config;

import java.io.Serializable;
import java.lang.annotation.Annotation;

public class ConfigImpl implements Config, Serializable {

	private static final long serialVersionUID = 0;
	private final String value;

	public ConfigImpl(String value) {
		this.value = (value == null) ? "name" : value;
	}

	public String value() {
		return this.value;
	}

	public int hashCode() {
		return (127 * "value".hashCode()) ^ value.hashCode();
	}

	public boolean equals(Object o) {
		if (!(o instanceof Config)) {
			return false;
		}
		Config other = (Config) o;
		return value.equals(other.value());
	}

	public String toString() {
		return "@" + Config.class.getName() + "(value=" + value + ")";
	}

	public Class<? extends Annotation> annotationType() {
		return Config.class;
	}
}