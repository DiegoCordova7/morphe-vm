package dev.eidos.vm.exception.stack;

public final class InvalidStackIndexException extends VMStackException {

  public InvalidStackIndexException(int index, int size) {
    super("Invalid stack index " + index + ". Current size: " + size);
  }
}