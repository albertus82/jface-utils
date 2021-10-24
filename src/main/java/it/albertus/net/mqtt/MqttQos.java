package it.albertus.net.mqtt;

import it.albertus.jface.JFaceMessages;

public enum MqttQos {

	AT_MOST_ONCE(0),
	AT_LEAST_ONCE(1),
	EXACTLY_ONCE(2);

	private static final String RESOURCE_KEY_PREFIX = "lbl.mqtt.qos.";

	private final byte value;
	private final String resourceKey;

	private MqttQos(final int value) {
		this.value = (byte) value;
		this.resourceKey = RESOURCE_KEY_PREFIX + value;
	}

	public byte getValue() {
		return value;
	}

	public String getDescription() {
		return JFaceMessages.get(resourceKey);
	}

}
