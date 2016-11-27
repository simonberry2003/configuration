package co.yaggle.configuration.domain;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@SuppressWarnings("serial")
@ToString
@Builder
public class ConfigurationItem implements Serializable {
	@Getter private String name;
	@Getter private String value;
	@Getter private Behaviour behaviour;

	public static class ConfigurationItemBuilder {
		private Behaviour behaviour = Behaviour.Overridden;
	}

	public boolean isOverride() {
		return behaviour == Behaviour.Overrides;
	}
}
