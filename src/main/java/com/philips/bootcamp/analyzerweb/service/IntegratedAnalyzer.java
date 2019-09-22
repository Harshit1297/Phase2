/*
 * The copyright of this file belongs to Koninklijke Philips N.V., 2019.
 */
package com.philips.bootcamp.analyzerweb.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.philips.bootcamp.analyzerweb.exceptions.FilePathNotValidException;
import com.philips.bootcamp.analyzerweb.model.Tool;
;

public class IntegratedAnalyzer extends Tool{

  private final List<Tool> tools = new ArrayList<>();


  public IntegratedAnalyzer() {}

  public IntegratedAnalyzer(String filepath) {
    super(filepath);
  }


  @Override
  public StringBuilder generateReport() throws FilePathNotValidException, IOException, InterruptedException {
    final StringBuilder finalReport = new StringBuilder();
    for (final Tool tool : tools) {
      final StringBuilder toolReport = tool.generateReport();
      issueCount = countIssues(toolReport, tool);
      finalReport.append(toolReport);
      finalReport.append("\n");
    }
    return finalReport;
  }

  @Override
  public void add(Tool tool) {
    tools.add(tool);
  }

  @Override
  public void remove(Tool tool) {
    tools.remove(tool);
  }

  @Override
  public int countIssues(StringBuilder report, Tool tool) {
    issueCount += tool.countIssues(report, tool);
    return issueCount;
  }

  @Override
  public String toString() {
    return null;
  }

}
