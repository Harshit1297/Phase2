/*
 * The copyright of this file belongs to Koninklijke Philips N.V., 2019.
 */
package com.philips.bootcamp.analyzerweb.exceptions;

@SuppressWarnings("serial")
public class FilePathNotValidException extends Exception {
  public FilePathNotValidException(String message) {
    super(message);
  }
}
