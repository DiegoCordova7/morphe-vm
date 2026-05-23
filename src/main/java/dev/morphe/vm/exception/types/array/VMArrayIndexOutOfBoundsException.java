package dev.morphe.vm.exception.types.array;

public final class VMArrayIndexOutOfBoundsException extends VMArrayException {

  public VMArrayIndexOutOfBoundsException(int index, int length) {
    super(buildMessage(index, length));
  }

  private static String buildMessage(int index, int length) {
    if (length == 0) {
      return "Array index out of bounds: " + index + ". Array is empty.";
    }

    return "Array index out of bounds: " + index + ". Valid range: 0.." + (length - 1);
  }
}