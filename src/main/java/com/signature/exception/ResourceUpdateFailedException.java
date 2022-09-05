package com.signature.exception;

public class ResourceUpdateFailedException extends Exception {

  private static final long serialVersionUID = 2L;

  public ResourceUpdateFailedException() {
    super();
  }

  public ResourceUpdateFailedException(String message) {
    super(message);
  }

  public ResourceUpdateFailedException(String message, Throwable cause) {
    super(message, cause);
  }

  public ResourceUpdateFailedException(Throwable cause) {
    super(cause);
  }

  public ResourceUpdateFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}