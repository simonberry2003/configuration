package co.yaggle.configuration.domain;

import java.io.Serializable;

import com.hazelcast.core.IMap;

import lombok.Builder;
import lombok.ToString;
import lombok.val;

@SuppressWarnings("serial")
@Builder
@ToString
public class Node implements Serializable {

	public static final String Default = "Default";

	private String name;
	private Node parent;

	/**
	 * Creates the configuration for this node
	 * @param configuration tree configuration
	 * @param nodeName
	 * @return
	 */
	public Configuration createConfiguration(IMap<String, Configuration> configuration) {

		Configuration nodeConfiguration = configuration.get(name);
		if (nodeConfiguration == null) {
			nodeConfiguration = new Configuration();
		}

		// Walk the tree to add all configuration values
		Node parent = this.parent;
		while (parent != null) {
			val parentConfig = configuration.get(parent.name);
			if (parentConfig != null) {
				nodeConfiguration.include(parentConfig);
			}
			parent = parent.parent;
		}
		return nodeConfiguration;
	}
}
