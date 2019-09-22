/*
 * The copyright of this file belongs to Koninklijke Philips N.V., 2019.
 */
package com.philips.bootcamp.analyzerweb.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.validation.Configuration;

public class ConfigFileReader {

  private static ConfigFileReader instance = null;

  private static final Properties properties;

  private static final String CONFIGURATION_FILE = "/application.properties";

  private ConfigFileReader() {

  }

  public static ConfigFileReader getReaderInstance() {
    if (instance == null) {
      instance = new ConfigFileReader();
    }
    return instance;
  }

  static {
    properties = new Properties();
    try (InputStream inputStream = Configuration.class.getResourceAsStream(CONFIGURATION_FILE)) {
      properties.load(inputStream);
    } catch (final IOException e) {
      throw new MyRuntimeException("Failed to read file " + CONFIGURATION_FILE, e);
    }
  }

  public static Map<String, String> getConfiguration() {
    // ugly workaround to get String as generics
    @SuppressWarnings("rawtypes")
    final Map temp = properties;

    @SuppressWarnings("unchecked")
    final Map<String, String> map = new HashMap<>(temp);
    
    // prevent the returned configuration from being modified
    return Collections.unmodifiableMap(map);
  }


  public String getConfigurationValue(String key) {
    return properties.getProperty(key);
  }

}
