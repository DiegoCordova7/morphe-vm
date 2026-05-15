package dev.eidos.vm.exception.stack;

public final class StackOverflowException extends VMStackException {

  public StackOverflowException(int capacity) {
    super("Stack overflow. Capacity: " + capacity);
  }
}