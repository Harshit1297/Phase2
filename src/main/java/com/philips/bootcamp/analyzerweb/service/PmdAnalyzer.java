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
import com.philips.bootcamp.analyzerweb.utils.Values;

public class PmdAnalyzer extends Tool{

  String pmdRuleset;

  public PmdAnalyzer() {
  }

  public PmdAnalyzer(String pmdRuleset) {
    this.pmdRuleset = pmdRuleset;
  }

  public PmdAnalyzer(String filepath, String pmdRuleset) {
    super(filepath);
    this.pmdRuleset = pmdRuleset;
  }

  public static PmdAnalyzer getObjectFromConfigFile() {
    final ConfigFileReader reader = ConfigFileReader.getReaderInstance();
    return new PmdAnalyzer(reader.getConfigurationValue("path"),
        reader.getConfigurationValue("pmdRuleset"));
  }

  @Override
  public StringBuilder generateReport() throws FilePathNotValidException, IOException, InterruptedException {
    if (FileValidator.isValidPath(filepath)) {
      final String[] cmdCommand = {Values.PMD_PATH, "-d", filepath,"-R", pmdRuleset, "-failOnViolation", "false"};
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
    return lines.length;
  }

  @Override
  public String toString() {
    return "";
  }

}
