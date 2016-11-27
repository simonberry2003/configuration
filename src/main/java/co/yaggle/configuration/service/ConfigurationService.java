package co.yaggle.configuration.service;

import co.yaggle.configuration.domain.Configuration;
import co.yaggle.configuration.domain.ConfigurationItem;

public interface ConfigurationService {
	void addNode(String nodeName, String parentNodeName);
	void set(String nodeName, ConfigurationItem configItem);
	Configuration get(String nodeName);
}
