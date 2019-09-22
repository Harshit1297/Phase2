/*
 * The copyright of this file belongs to Koninklijke Philips N.V., 2019.
 */
package com.philips.bootcamp.analyzerweb.utils;

import java.io.File;

import org.apache.logging.log4j.Logger;

public class FileValidator {
	private static Logger logger;
	private FileValidator() {
		
	}
  public static boolean isValidPath(String filepath) {

    boolean exists = false;
    try
    {
      final File tempFile = new File(filepath);

      exists = tempFile.exists();
    }
    catch(final Exception e)
    {
      logger.debug("Hey, an error here!");
      exists = false;
    }
    return exists;
  }

}
