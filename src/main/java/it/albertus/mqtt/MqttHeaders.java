package it.albertus.mqtt;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class MqttHeaders extends LinkedHashMap<String, String> {

	private static final long serialVersionUID = -5771033947201738969L;

	@Override
	public boolean containsKey(final Object key) {
		final String keyStr = key.toString();
		for (final String k : keySet()) {
			if (keyStr.equalsIgnoreCase(k)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String get(final Object key) {
		final String keyStr = key.toString();
		for (final Entry<String, String> e : entrySet()) {
			if (keyStr.equalsIgnoreCase(e.getKey())) {
				return e.getValue();
			}
		}
		return null;
	}

}
