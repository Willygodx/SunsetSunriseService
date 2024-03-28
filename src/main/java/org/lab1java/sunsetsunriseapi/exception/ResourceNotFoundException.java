package org.lab1java.sunsetsunriseapi.exception;

/**
 * Exception thrown when a requested resource is not found.
 */
public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException(String message) {
    super(message);
  }
}

