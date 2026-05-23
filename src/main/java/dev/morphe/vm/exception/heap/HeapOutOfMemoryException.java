package dev.morphe.vm.exception.heap;

public final class HeapOutOfMemoryException extends VMHeapException {

  public HeapOutOfMemoryException(int capacity) {
    super("Heap is full. Capacity: " + capacity);
  }
}