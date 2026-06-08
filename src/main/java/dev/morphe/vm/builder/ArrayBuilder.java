package dev.morphe.vm.builder;

import dev.morphe.vm.instructions.opcodes.ArrayOpCode;

/**
 * Builder for emitting array-related instructions in the VM program.
 * <p>
 * Provides convenient methods for creating arrays, accessing elements,
 * setting elements, and retrieving array length.
 * Each method emits the corresponding {@link ArrayOpCode} to the instruction list.
 * </p>
 * <p>
 * Intended for internal use by {@link VMProgramBuilder}.
 * </p>
 */
public final class ArrayBuilder {

  private final InstructionEmitter emitter;

  /**
   * Creates a new array builder with the given instruction emitter.
   *
   * @param emitter the instruction emitter used to append array operations
   */
  ArrayBuilder(InstructionEmitter emitter) {
    this.emitter = emitter;
  }

  /**
   * Emits an ARRAY_NEW instruction to create a new array.
   * <p>
   * The size must be provided on the stack before calling this instruction.
   * The resulting array index is pushed onto the stack.
   * </p>
   */
  public void newArray() {
    emitter.emit(ArrayOpCode.ARRAY_NEW);
  }

  public void newMultiArray(int dimensions) {
    emitter.emit(ArrayOpCode.ARRAY_NEW, dimensions);
  }

  /**
   * Emits an ARRAY_GET instruction to retrieve an element from an array.
   * <p>
   * Pops the array index and element index from the stack, then pushes
   * the element's heap index onto the stack.
   * </p>
   */
  public void get() {
    emitter.emit(ArrayOpCode.ARRAY_GET);
  }

  /**
   * Emits an ARRAY_SET instruction to set an element in an array.
   * <p>
   * Pops the value index, element index, and array index from the stack
   * and updates the array.
   * </p>
   */
  public void set() {
    emitter.emit(ArrayOpCode.ARRAY_SET);
  }

  /**
   * Emits an ARRAY_LENGTH instruction to get the length of an array.
   * <p>
   * Pops the array index from the stack and pushes the length as a VMInteger.
   * </p>
   */
  public void length() {
    emitter.emit(ArrayOpCode.ARRAY_LENGTH);
  }
}