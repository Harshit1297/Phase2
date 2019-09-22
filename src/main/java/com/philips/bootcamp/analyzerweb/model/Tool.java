/*
 * The copyright of this file belongs to Koninklijke Philips N.V., 2019.
 */
package com.philips.bootcamp.analyzerweb.model;

import java.io.IOException;
import com.philips.bootcamp.analyzerweb.exceptions.FilePathNotValidException;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class Tool {

  protected String filepath;
  protected int issueCount;

  public Tool() {}

  public int getIssueCount() {
    return issueCount;
  }

  public String getFilepath() {
    return filepath;
  }

  public Tool(String filepath) {
    this.filepath = filepath;
    this.issueCount = 0;
  }

  public abstract StringBuilder generateReport() throws FilePathNotValidException, IOException, InterruptedException ;

  public void add(Tool tool) {
    throw new UnsupportedOperationException("Cannot add tool by default");
  }

  public void remove(Tool tool) {
    throw new UnsupportedOperationException("Cannot remove tool by default");
  }

  @Override
  public abstract String toString();

  public abstract int countIssues(StringBuilder report, Tool tool);
}
