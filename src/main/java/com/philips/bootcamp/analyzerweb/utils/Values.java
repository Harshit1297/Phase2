/*
 * The copyright of this file belongs to Koninklijke Philips N.V., 2019.
 */
package com.philips.bootcamp.analyzerweb.utils;

public class Values {

  private Values() {
  }

  static ConfigFileReader reader = ConfigFileReader.getReaderInstance();
  public static final String FILE_PATH = reader.getConfigurationValue("path");
  public static final String PMD_RULESET = reader.getConfigurationValue("pmdRuleset");
  public static final String CHECKSTYLE_PATH = reader.getConfigurationValue("checkstylePath");
  public static final String CHECKSTYLE_RULESET = reader.getConfigurationValue("checkstyleRuleset");
  public static final String ERROR_FILE_NOT_FOUND = "Error: File not found";
  public static final String TEST_VALID_FILE_PATH = reader.getConfigurationValue("test_valid_path");
  public static final String TEST_INVALID_FILE_PATH = reader.getConfigurationValue("test_invalid_path");
  public static final String SIMIAN_PATH = reader.getConfigurationValue("simianPath");
  public static final String PMD_PATH = reader.getConfigurationValue("pmdPath");

}