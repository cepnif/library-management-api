package config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigLoader is a utility class for loading and accessing configuration properties
 * from the application properties file. It provides methods to retrieve specific
 * configuration values, such as the base URL, username, password, and token expiration time.
 */
public class ConfigLoader {

    private static final Logger logger = LogManager.getLogger(ConfigLoader.class);
    private static final Properties properties = new Properties();

    // Define constants for property keys
    private static final String BASE_URL_KEY = "base.url";
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private static final String TOKEN_EXPIRY_KEY = "token.expiration.minutes";

    // Load properties from the configuration file
    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config/application.properties")) {
            if (input == null) {
                logger.error("Unable to find application.properties");
                throw new RuntimeException("Configuration file application.properties not found in config directory");
            }
            properties.load(input);
            logger.info("Configuration file loaded successfully");
        } catch (IOException ex) {
            logger.error("Error loading configuration", ex);
            throw new RuntimeException("Error loading configuration", ex);
        }
    }

    /**
     * Retrieves the base URL for the application from the configuration file.
     * @return The base URL as a String, or null if not found.
     */
    public static String getBaseUrl() {
        return getProperty(BASE_URL_KEY);
    }

    /**
     * Retrieves the username from the configuration file.
     * @return The username as a String, or null if not found.
     */
    public static String getUsername() {
        return getProperty(USERNAME_KEY);
    }

    /**
     * Retrieves the password from the configuration file.
     * @return The password as a String, or null if not found.
     */
    public static String getPassword() {
        return getProperty(PASSWORD_KEY);
    }

    /**
     * Retrieves the token expiration time in minutes from the configuration file.
     * @return The token expiration time as a String, or null if not found.
     */
    public static String getTokenExpiryTime() {
        return getProperty(TOKEN_EXPIRY_KEY);
    }

    /**
     * Retrieves a property value from the loaded properties file.
     * Logs a warning if the property is missing or empty.
     * @param key The key of the property to retrieve.
     * @return The property value as a String, or null if the key is not found or is empty.
     */
    private static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isEmpty()) {
            logger.warn("Property '{}' is missing or empty in configuration", key);
        }
        return value;
    }
}
