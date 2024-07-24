package org.kaib.discover.maven;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

public enum Configuration {

	INSTANCE;

	private final Map<ConfigKey, String> settings = new EnumMap<>(ConfigKey.class);

	public void loadConfiguration(String configFilePath) {

		Properties properties = new Properties();
		try (FileInputStream inputStream = new FileInputStream(configFilePath)) {

			properties.load(inputStream);
			loadConfiguration(properties);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadConfiguration(final Properties properties) {

		for (final String propKey : properties.stringPropertyNames()) {

			final ConfigKey configKey = ConfigKey.lookup(propKey);
			final String configValue = properties.getProperty(propKey);
			settings.put(configKey, configValue);
		}
	}
	
	public String getSetting(final ConfigKey key) {
		return settings.get(key);
	}
}
