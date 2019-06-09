package com.thuisapp.mqtt.util;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;
import java.util.Arrays;
import java.util.stream.Collectors;

@ApplicationScoped
public class MqttUtil {

	@Inject
	@ConfigProperty(name = "mqtt.scheme")
	String mqttScheme;

	@Inject
	@ConfigProperty(name = "mqtt.host")
	String mqttHost;

	@Inject
	@ConfigProperty(name = "mqtt.port")
	int mqttPort;

	@Inject
	@ConfigProperty(name = "mqtt.topic_prefix")
	String mqttTopicPrefix;

	public String buildMqttUri() {
		return UriBuilder.fromPath("")
				.scheme(mqttScheme)
				.host(mqttHost)
				.port(mqttPort)
				.build()
				.toString();
	}

	public String buildTopic(String subject, String topic) {
		return new StringBuilder()
				.append(mqttTopicPrefix)
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
