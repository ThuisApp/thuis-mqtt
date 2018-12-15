package com.thuisapp.mqtt;

import com.thuisapp.mqtt.util.MqttUtil;
import lombok.extern.java.Log;
import org.eclipse.microprofile.config.Config;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.logging.Level;

@Log
@Singleton
public class MqttClient implements MqttCallbackExtended {

	public static final String WILL_TOPIC = "connected";

	@Inject
	private MqttUtil mqttUtil;

	@Inject
	private Config config;

	private IMqttAsyncClient client;

	public void init(@Observes @Initialized(ApplicationScoped.class) Object event) {
		try {
			client = new MqttAsyncClient(
					mqttUtil.buildMqttUri(),
					config.getValue("mqtt.client_id", String.class),
					new MemoryPersistence()
			);
			MqttConnectOptions options = new MqttConnectOptions();
			config.getOptionalValue("mqtt.username", String.class).ifPresent(options::setUserName);
			config.getOptionalValue("mqtt.password", String.class).map(String::toCharArray).ifPresent(options::setPassword);
			options.setWill(WILL_TOPIC, "0".getBytes(), 2, true);
			client.setCallback(this);
			client.connect(options).waitForCompletion();
		} catch (MqttException e) {
			log.log(Level.WARNING, "Could not connect to MQTT broker", e);
		}
	}


	public void publish(String subject, String topic, String payload) {
		try {
			MqttMessage message = new MqttMessage(payload.getBytes());
			message.setRetained(true);
			client.publish(mqttUtil.buildTopic(subject, topic), message);
		} catch (MqttException e) {
			log.log(Level.WARNING, "Could not send MQTT message", e);
		}
	}

	@Override
	public void connectComplete(boolean reconnect, String serverURI) {
		publish("", WILL_TOPIC, "1");
	}

	@Override
	public void connectionLost(Throwable cause) {
		publish("", WILL_TOPIC, "0");
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) {
		// Do nothing
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// Do nothing
	}

	@PreDestroy
	public void destroy() {
		try {
			client.disconnect();
		} catch (MqttException e) {
			log.log(Level.WARNING, "Could not disconnect MQTT broker", e);
		}
	}
}
