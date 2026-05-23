package dev.morphe.vm.builder;

import dev.morphe.vm.instructions.opcodes.IOOpCode;

/**
 * Builder for emitting I/O instructions in the VM program.
 * <p>
 * Provides simple methods to emit print, read, and input instructions.
 * Intended for internal use by {@link VMProgramBuilder}.
 * </p>
 */
public final class IOBuilder {

  private final InstructionEmitter emitter;

  /**
   * Creates a new I/O builder.
   *
   * @param emitter the instruction emitter used to append I/O operations
   */
  IOBuilder(InstructionEmitter emitter) {
    this.emitter = emitter;
  }

  /** Emits a PRINT instruction to output the top value of the stack. */
  public void print() {
    emitter.emit(IOOpCode.PRINT);
  }

  /** Emits a READ instruction to read a value from input and push it onto the stack. */
  public void read() {
    emitter.emit(IOOpCode.READ);
  }

  /** Emits an INPUT instruction to read a line of input as a string and push it onto the stack. */
  public void input() {
    emitter.emit(IOOpCode.INPUT);
  }
}