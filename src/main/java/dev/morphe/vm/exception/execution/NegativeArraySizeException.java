package dev.morphe.vm.exception.execution;

public final class NegativeArraySizeException extends VMExecutionException {
  public NegativeArraySizeException(int size) {
    super("Negative array size: " + size);
  }
}