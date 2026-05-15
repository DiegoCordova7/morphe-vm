package dev.eidos.vm.exception.types.array;

public final class VMArrayIndexOutOfBoundsException extends VMArrayException {

  public VMArrayIndexOutOfBoundsException(int index, int length) {
    super("Array index out of bounds: " + index + ". Valid range: 0.." + (length - 1));
  }
}