package dev.eidos.vm.exception.heap;

public final class InvalidHeapAddressException extends VMHeapException {

  public InvalidHeapAddressException(int index, int capacity) {
    super("Invalid heap address " + index + ". Valid range: 0.." + (capacity - 1));
  }

  public InvalidHeapAddressException(int index) {
    super("Invalid or unallocated heap address: " + index);
  }
}