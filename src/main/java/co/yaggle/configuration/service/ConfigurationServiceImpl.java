package co.yaggle.configuration.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import co.yaggle.configuration.domain.Configuration;
import co.yaggle.configuration.domain.ConfigurationItem;
import co.yaggle.configuration.domain.Node;
import lombok.val;

public class ConfigurationServiceImpl implements ConfigurationService {

	private final HazelcastInstance hazelcast;

	public ConfigurationServiceImpl(HazelcastInstance hazelcast) {
		this.hazelcast = hazelcast;
	}

	@Override
	public void addNode(String nodeName, String parentNodeName) {

		IMap<String, Node> configurationTree = hazelcast.getMap("configurationTree");

		// Create default (root) node if it does not exist
		if (!configurationTree.containsKey(Node.Default)) {
			val node = Node.builder()
				.name(Node.Default)
				.build();
			configurationTree.put(Node.Default, node);
		}

		if (configurationTree.containsKey(nodeName)) {
			throw new IllegalArgumentException("Node exists");
		}

		val parentNode = configurationTree.get(parentNodeName);
		if (parentNode == null) {
			throw new IllegalArgumentException("Parent node does not exist");
		}

		val node = Node.builder()
			.parent(parentNode)
			.name(nodeName)
			.build();
		configurationTree.put(nodeName, node);
	}

	@Override
	public void set(String nodeName, ConfigurationItem configItem) {

		IMap<String, Node> configurationTree = hazelcast.getMap("configurationTree");
		val node = configurationTree.get(nodeName);
		if (node == null) {
			throw new IllegalArgumentException("Node does not exist");
		}

		IMap<String, Configuration> configuration = hazelcast.getMap("configuration");
		Configuration nodeConfig = configuration.get(nodeName);
		if (nodeConfig == null) {
			nodeConfig = new Configuration();
		}
		nodeConfig.add(configItem);
		configuration.put(nodeName, nodeConfig);
	}

	@Override
	public Configuration get(String nodeName) {

		IMap<String, Configuration> configuration = hazelcast.getMap("configuration");

		IMap<String, Node> configurationTree = hazelcast.getMap("configurationTree");
		val node = configurationTree.get(nodeName);
		if (node == null) {
			throw new IllegalArgumentException("Node does not exist");
		}

		return node.createConfiguration(configuration);
	}
}
