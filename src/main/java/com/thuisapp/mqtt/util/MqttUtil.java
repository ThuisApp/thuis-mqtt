package com.thuisapp.mqtt.util;

import org.eclipse.microprofile.config.Config;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;
import java.util.Arrays;
import java.util.stream.Collectors;

@ApplicationScoped
public class MqttUtil {

	@Inject
	Config config;

	public String buildMqttUri() {
		return UriBuilder.fromPath("")
				.scheme(config.getValue("mqtt.scheme", String.class))
				.host(config.getValue("mqtt.host", String.class))
				.port(config.getValue("mqtt.port", Integer.class))
				.build()
				.toString();
	}

	public String buildTopic(String subject, String topic) {
		return new StringBuilder()
				.append(config.getValue("mqtt.topic_prefix", String.class))
				.append(cleanSubject(subject))
				.append(topic)
				.toString();
	}

	public static String cleanSubject(String rawSubject) {
		if (rawSubject == null) return null;

		return lowercaseFirst(Arrays.stream(rawSubject.split("(_|\\W)+"))
			.map(MqttUtil::uppercaseFirst)
			.collect(Collectors.joining()));
	}

	private static String uppercaseFirst(String s) {
		if (s == null) return null;
		if (s.length() == 0) return s;
		if (s.length() == 1) return s.toUpperCase();

		return s.substring(0, 1).toUpperCase() +
				s.substring(1);
	}

	private static String lowercaseFirst(String s) {
		if (s == null) return null;
		if (s.length() == 0) return s;
		if (s.length() == 1) return s.toLowerCase();

		return s.substring(0, 1).toLowerCase() +
				s.substring(1);
	}
}
