package es.enaire.apptest;

/**
 * Exception throw by the application when a there is a create file exception.
 */
public class FileException extends Exception {

  public FileException(String message) {
    super(message);
  }

}
