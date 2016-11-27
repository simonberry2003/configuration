package co.yaggle.configuration.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.ToString;
import lombok.val;

@SuppressWarnings("serial")
@ToString
public class Configuration implements Serializable {

	private Map<String, ConfigurationItem> items = new HashMap<>();

	public void add(ConfigurationItem configItem) {
		items.put(configItem.getName(), configItem);
	}

	/**
	 * Include all the parent configuration items in this configuration.
	 * Items are copied from the parent if they either don't exist or the parent item
	 * overrides the value.
	 * @param parent
	 */
	public void include(Configuration parent) {
		for (val item : parent.items.entrySet()) {
			if (!items.containsKey(item.getKey()) || item.getValue().isOverride()) {
				items.put(item.getKey(), item.getValue());
			}
		}
	}

	public String get(String key) {
		return items.get(key).getValue();
	}
}
