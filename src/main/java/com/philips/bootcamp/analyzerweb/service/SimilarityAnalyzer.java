/*
 * The copyright of this file belongs to Koninklijke Philips N.V., 2019.
 */
package com.philips.bootcamp.analyzerweb.service;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.philips.bootcamp.analyzerweb.exceptions.FilePathNotValidException;
import com.philips.bootcamp.analyzerweb.model.Tool;
import com.philips.bootcamp.analyzerweb.utils.CommandLineExecutor;
import com.philips.bootcamp.analyzerweb.utils.FileValidator;

public class SimilarityAnalyzer extends Tool {

  String simianPath;

  public SimilarityAnalyzer() {

  }

  public SimilarityAnalyzer(String simianPath) {
    this.simianPath = simianPath;
  }

  public SimilarityAnalyzer(String filepath, String simianPath) {
    super(filepath);
    this.simianPath = simianPath;
  }

  @Override
  public StringBuilder generateReport()
      throws FilePathNotValidException, IOException, InterruptedException {
    if (FileValidator.isValidPath(filepath)) {
      if (new File(filepath).isDirectory()) {
        filepath += "*.java";
      }
      final String[] cmdCommand = {"java", "-jar", simianPath, filepath};
      final StringBuilder outputReport = new StringBuilder(CommandLineExecutor.runShellCommand(cmdCommand));
      issueCount = countIssues(outputReport, this);
      return outputReport;
    } else {
      throw new FilePathNotValidException("Error: incorrect path/file not found");
    }
  }




  @Override
  public int countIssues(StringBuilder report, Tool tool) {
    final Pattern p = Pattern.compile("[0-9]+(?= duplicate lines)");
    final Matcher m = p.matcher(report.toString());
    int simianCount = 0;
    while (m.find()) {
      simianCount = Integer.parseInt(m.group());
    }
    return simianCount;
  }

  @Override
  public String toString() {
    // TODO Auto-generated method stub
    return null;
  }

}
