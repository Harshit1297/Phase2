/*
 * The copyright of this file belongs to Koninklijke Philips N.V., 2019.
 */
package com.philips.bootcamp.analyzerweb.service;

import java.io.IOException;
import com.philips.bootcamp.analyzerweb.exceptions.FilePathNotValidException;
import com.philips.bootcamp.analyzerweb.model.Tool;
import com.philips.bootcamp.analyzerweb.utils.CommandLineExecutor;
import com.philips.bootcamp.analyzerweb.utils.ConfigFileReader;
import com.philips.bootcamp.analyzerweb.utils.FileValidator;

public class CheckstyleAnalyzer extends Tool {

  private final String checkstylePath;
  private final String checkstyleRuleset;

  public CheckstyleAnalyzer() {
    this.checkstylePath = null;
    this.checkstyleRuleset = null;
  }

  public CheckstyleAnalyzer(String checkstylePath, String checkstyleRuleset) {
    this.checkstylePath = checkstylePath;
    this.checkstyleRuleset = checkstyleRuleset;
  }

  public CheckstyleAnalyzer(String filepath, String checkstylePath, String checkstyleRuleset) {
    super(filepath);

    this.checkstylePath = checkstylePath;
    this.checkstyleRuleset = checkstyleRuleset;
  }

  public static CheckstyleAnalyzer getObjectFromConfigFile() {
    final ConfigFileReader reader = ConfigFileReader.getReaderInstance();
    return new CheckstyleAnalyzer(reader.getConfigurationValue("path"), reader.getConfigurationValue("checkstylePath"),
        reader.getConfigurationValue("checkstyleRuleset"));
  }

  @Override
  public StringBuilder generateReport() throws FilePathNotValidException, IOException, InterruptedException {
    if (FileValidator.isValidPath(filepath)) {
      final String[] cmdCommand = { "java", "-jar", checkstylePath, "-c", checkstyleRuleset, filepath };
      final StringBuilder outputReport = new StringBuilder(CommandLineExecutor.runShellCommand(cmdCommand));
      issueCount = countIssues(outputReport, this);
      return outputReport;
    } else {
      throw new FilePathNotValidException("Error: incorrect path/file not found");
    }
  }

  @Override
  public int countIssues(StringBuilder report, Tool tool) {
    final String[] lines = report.toString().split("\r\n|\r|\n");
    return lines.length - 2;
  }

  @Override
  public String toString() {
    // TODO Auto-generated method stub
    return null;
  }

}
