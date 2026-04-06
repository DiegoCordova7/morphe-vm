package dev.eidos.vm.builder;

import dev.eidos.vm.instructions.opcodes.LogicalOpCode;

/**
 * Builder for emitting logical operations in the VM program.
 * <p>
 * Supports AND, OR, and NOT operations on boolean values.
 * Intended for internal use by {@link VMProgramBuilder}.
 * </p>
 */
public final class LogicalBuilder {

  private final InstructionEmitter emitter;

  /**
   * Creates a new logical builder.
   *
   * @param emitter the instruction emitter used to append logical operations
   */
  LogicalBuilder(InstructionEmitter emitter) {
    this.emitter = emitter;
  }

  /** Emits an OR instruction to perform logical OR on the top two boolean values of the stack. */
  public void or() {
    emitter.emit(LogicalOpCode.OR);
  }

  /** Emits an AND instruction to perform logical AND on the top two boolean values of the stack. */
  public void and() {
    emitter.emit(LogicalOpCode.AND);
  }

  /** Emits a NOT instruction to perform logical NOT on the top boolean value of the stack. */
  public void not() {
    emitter.emit(LogicalOpCode.NOT);
  }
}