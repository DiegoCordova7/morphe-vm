package dev.morphe.vm.exception.types.array;

public final class VMNegativeArraySizeException extends VMArrayException {

  public VMNegativeArraySizeException(int size) {
    super("Negative array size: " + size);
  }
}