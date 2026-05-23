package dev.morphe.vm.builder;

import dev.morphe.vm.instructions.opcodes.ComparisonOpCode;

/**
 * Builder for emitting comparison-related instructions in the VM program.
 * <p>
 * Provides convenient methods for comparing values on the stack.
 * Supported comparison operations: EQ, NEQ, GT, LT, GTE, LTE.
 * Each method emits the corresponding {@link ComparisonOpCode} to the instruction list.
 * </p>
 * <p>
 * Intended for internal use by {@link VMProgramBuilder}.
 * </p>
 */
public final class ComparisonBuilder {

  private final InstructionEmitter emitter;

  /**
   * Creates a new comparison builder with the given instruction emitter.
   *
   * @param emitter the instruction emitter used to append comparison operations
   */
  ComparisonBuilder(InstructionEmitter emitter) {
    this.emitter = emitter;
  }

  /** Emits a GT instruction to check if the first operand is greater than the second. */
  public void gt() {
    emitter.emit(ComparisonOpCode.GT);
  }

  /** Emits a GTE instruction to check if the first operand is greater than or equal to the second. */
  public void gte() {
    emitter.emit(ComparisonOpCode.GTE);
  }

  /** Emits a LT instruction to check if the first operand is less than the second. */
  public void lt() {
    emitter.emit(ComparisonOpCode.LT);
  }

  /** Emits a LTE instruction to check if the first operand is less than or equal to the second. */
  public void lte() {
    emitter.emit(ComparisonOpCode.LTE);
  }

  /** Emits an EQ instruction to check if the two operands are equal. */
  public void eq() {
    emitter.emit(ComparisonOpCode.EQ);
  }

  /** Emits a NEQ instruction to check if the two operands are not equal. */
  public void neq() {
    emitter.emit(ComparisonOpCode.NEQ);
  }
}