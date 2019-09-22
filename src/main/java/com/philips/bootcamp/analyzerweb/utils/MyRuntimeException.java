package com.philips.bootcamp.analyzerweb.utils;

public class MyRuntimeException extends RuntimeException {
  public MyRuntimeException(String message, Exception e) {
    super(message, e);
  }
}
