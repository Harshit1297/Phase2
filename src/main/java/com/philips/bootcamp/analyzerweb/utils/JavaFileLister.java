/*
 * The copyright of this file belongs to Koninklijke Philips N.V., 2019.
 */
package com.philips.bootcamp.analyzerweb.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.Logger;

public class JavaFileLister {
	 private static Logger logger;

  public List<String> javaFilefilter(String dir){
    List<String> result = null;
    try (final Stream<Path> walk = Files.walk(Paths.get(dir))) {

      result = walk.map(x -> x.toString())
          .filter(f -> f.endsWith(".java")).collect(Collectors.toList());

    } catch (final IOException e) {
    	 logger.error("something happened here");
    }
    return result; 
  }
}
