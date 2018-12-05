package com.thuisapp.mqtt.util;

import org.eclipse.microprofile.config.Config;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;

@Stateless
public class MqttUtil {

	@Inject
	private Config config;

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
				.append(subject)
				.append(topic)
				.toString();
	}

}
