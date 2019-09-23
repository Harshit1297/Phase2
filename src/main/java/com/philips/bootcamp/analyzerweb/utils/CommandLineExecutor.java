/*
 * The copyright of this file belongs to Koninklijke Philips N.V., 2019.
 */
package com.philips.bootcamp.analyzerweb.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandLineExecutor {
  private CommandLineExecutor() {
  }

  public static StringBuilder runShellCommand(String[] args)
      throws IOException {
    final ProcessBuilder builder = new ProcessBuilder();
    builder.command(args);
    builder.directory(new File(System.getProperty("user.home")));
    final Process process = builder.start();
    final BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line;
    final StringBuilder cmdOutput = new StringBuilder();
    while ((line = br.readLine()) != null) {
      cmdOutput.append(String.format("%s%n", line));
    }
    return cmdOutput;
  }
}
