package dev.eidos.vm.builder;

import dev.eidos.vm.core.VMException;
import dev.eidos.vm.core.types.*;
import dev.eidos.vm.instructions.opcodes.StackOpCode;

/**
 * Builder for emitting stack-related instructions in the VM program.
 * <p>
 * Supports pushing and popping values, duplicating stack elements, and
 * converting literals into heap-allocated VM values.
 * Intended for internal use by {@link VMProgramBuilder}.
 * </p>
 */
public final class StackBuilder {

  private final InstructionEmitter emitter;
  private final ProgramState state;

  /**
   * Creates a new stack builder.
   *
   * @param emitter the instruction emitter used to append stack operations
   * @param state the current program state
   */
  StackBuilder(InstructionEmitter emitter, ProgramState state) {
    this.emitter = emitter;
    this.state = state;
  }

  /**
   * Converts a literal into a heap-allocated VM value and returns its heap index.
   *
   * @param value the literal value (Integer, Double, String, or IVMValue)
   * @return the heap index of the allocated value
   * @throws VMException if the literal type is unsupported
   */
  private int literal(Object value) {
    IVMValue v;
    if (value instanceof IVMValue iv) v = iv;
    else if (value instanceof Integer i) v = new VMInteger(i);
    else if (value instanceof Double d) v = new VMDouble(d);
    else if (value instanceof String s) v = new VMString(s);
    else throw new VMException("Unsupported literal: " + value);
    return state.heap.alloc(v);
  }

  /**
   * Pushes a literal value onto the stack.
   *
   * @param value the value to push
   */
  public void pushLiteral(IVMValue value) {
    push(literal(value));
  }

  /**
   * Pushes a heap-allocated value onto the stack by index.
   *
   * @param heapIndex the index of the value in the VM heap
   */
  public void push(int heapIndex) {
    emitter.emit(StackOpCode.PUSH, heapIndex);
  }

  /** Pops the top value from the stack. */
  public void pop() {
    emitter.emit(StackOpCode.POP);
  }

  /** Duplicates the top value on the stack. */
  public void dup() {
    emitter.emit(StackOpCode.DUP);
  }
}