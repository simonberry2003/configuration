package co.yaggle.configuration;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import co.yaggle.configuration.domain.Behaviour;
import co.yaggle.configuration.domain.Configuration;
import co.yaggle.configuration.domain.ConfigurationItem;
import co.yaggle.configuration.domain.Node;
import co.yaggle.configuration.service.ConfigurationServiceImpl;
import lombok.val;

public class Main {

	private static HazelcastInstance instance;

	public static void main(String[] args) {
		Config cfg = new Config();
        instance = Hazelcast.newHazelcastInstance(cfg);

        val service = new ConfigurationServiceImpl(instance);
        service.addNode("c1", Node.Default);
        service.set(Node.Default, ConfigurationItem.builder().name("x").value("1").build());
        service.set(Node.Default, ConfigurationItem.builder().name("y").value("2").behaviour(Behaviour.Overrides).build());
        service.set(Node.Default, ConfigurationItem.builder().name("z").value("3").build());

        service.set("c1", ConfigurationItem.builder().name("y").value("3").build());
        Configuration configuration = service.get("c1");
        service.set("c1", ConfigurationItem.builder().name("z").value("4").build());
        configuration = service.get("c1");

        service.addNode("c2", "c1");
        configuration = service.get("c2");

        service.set(Node.Default, ConfigurationItem.builder().name("y").value("2").build());

        configuration = service.get("c2");
        val x = configuration.get("x");
	}
}
