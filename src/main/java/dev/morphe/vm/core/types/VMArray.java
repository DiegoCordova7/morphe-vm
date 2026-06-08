package dev.morphe.vm.core.types;

import dev.morphe.vm.core.VMHeap;
import dev.morphe.vm.exception.types.array.VMArrayIndexOutOfBoundsException;

import java.util.Arrays;
import java.util.List;

/**
 * Represents an array of heap references in the VM.
 * <p>
 * Each element in the array stores an index pointing to a value in {@link VMHeap}.
 * Provides array operations, bounds checking, and string representation using the heap.
 * </p>
 */
public final class VMArray implements IVMValue {

  private final int[] elements;

  /**
   * Creates a new VMArray with the specified size.
   *
   * @param size The number of elements in the array.
   */
  public VMArray(int size) {
    this.elements = new int[size];
  }

  /**
   * Returns the length of the array.
   *
   * @return The number of elements.
   */
  public int length() {
    return elements.length;
  }

  /**
   * Returns a list of the heap indices stored in the array.
   *
   * @return A list of integers representing heap indices.
   */
  public List<Integer> getElements() {
    return Arrays.stream(elements).boxed().toList();
  }

  /**
   * Retrieves the heap index at the given array position.
   *
   * @param index The position in the array.
   * @return The heap index stored at that position.
   * @throws VMArrayIndexOutOfBoundsException If the index is out of bounds.
   */
  public int get(int index) {
    checkBounds(index);
    return elements[index];
  }

  /**
   * Sets the heap index at the given array position.
   *
   * @param index     The position in the array.
   * @param heapIndex The heap index to store.
   * @throws VMArrayIndexOutOfBoundsException If the index is out of bounds.
   */
  public void set(int index, int heapIndex) {
    checkBounds(index);
    elements[index] = heapIndex;
  }

  /**
   * Checks if the given index is within the array bounds.
   *
   * @param index The index to check.
   * @throws VMArrayIndexOutOfBoundsException If the index is out of bounds.
   */
  private void checkBounds(int index) {
    if (index < 0 || index >= elements.length) {
      throw new VMArrayIndexOutOfBoundsException(index, elements.length);
    }
  }

  /**
   * Returns a string representation of the array by resolving heap values.
   *
   * @param heap The VMHeap used to resolve heap indices to actual values.
   * @return A string representing the array contents, e.g., "[val1, val2, ...]".
   */
  public String toString(VMHeap heap) {
    StringBuilder sb = new StringBuilder("[");
    for (int i = 0; i < elements.length; i++) {
      int index = elements[i];
      IVMValue val = heap.get(index);
      sb.append(val);

      if (i < elements.length - 1) sb.append(", ");
    }
    sb.append("]");
    return sb.toString();
  }

  @Override
  public String toString() {
    return "Array" + Arrays.toString(elements);
  }
}