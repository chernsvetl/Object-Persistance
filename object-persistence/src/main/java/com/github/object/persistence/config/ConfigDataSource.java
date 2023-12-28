package com.github.object.persistence.config;


import com.github.object.persistence.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public final class ConfigDataSource {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Properties properties = loadConfig();

    private static final String CONFIG_NAME = "persistence.properties";
    private static final String URL = "persistence.url";
    private static final String USERNAME = "persistence.username";
    private static final String PASSWORD = "persistence.password";
    private static final String DRIVER = "persistence.driver";
    private static final String IS_INIT = "persistence.initialize";
    private static final String THREAD_POOL_SIZE = "persistence.thread-pool-size";
    private static final ConfigDataSource INSTANCE = new ConfigDataSource();

    private ConfigDataSource() {
    }

    private Properties loadConfig() {
        try {
            Properties prop = new Properties();
            prop.load(this.getClass().getClassLoader().getResourceAsStream(CONFIG_NAME));
            validateProperties(prop);
            return prop;
        } catch (IOException exception) {
            logger.error("Error while loading config: ", exception);
            throw new NullPointerException("Config is not present");
        }
    }

    private void validateProperties(Properties prop) {
        if (prop.isEmpty()) {
            throw new ValidationException("Config file is empty");
        }
        validateProperties(prop, URL, USERNAME, PASSWORD, IS_INIT, THREAD_POOL_SIZE);
        validateProperties(prop, DRIVER);
    }

    public String getDataSourceUrl() {
        return properties.getProperty(URL);
    }

    public String getUsername() {
        return properties.getProperty(USERNAME);
    }

    public String getPassword() {
        return properties.getProperty(PASSWORD);
    }

    public String getDriver() {
        return properties.getProperty(DRIVER);
    }

    public int getThreadPoolSize() {
        return Integer.parseInt((properties.getProperty(THREAD_POOL_SIZE)));
    }

    public boolean isInitializeNeeded() {
        return Boolean.parseBoolean(properties.getProperty(IS_INIT));
    }

    public static ConfigDataSource getInstance() {
        return INSTANCE;
    }

    private void validateProperties(Properties prop, String... propertyNames) {
        for (String property : propertyNames) {
            if (prop.get(property) == null) {
                throw new ValidationException(String.format("Required setting %s is not present", property));
            }
        }
    }
}

