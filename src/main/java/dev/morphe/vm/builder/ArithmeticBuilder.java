package dev.morphe.vm.builder;

import dev.morphe.vm.instructions.opcodes.ArithmeticOpCode;

/**
 * Builder for emitting arithmetic instructions in the VM program.
 * <p>
 * Provides convenient methods for all basic arithmetic operations:
 * addition, subtraction, multiplication, division, and modulo.
 * Each method emits the corresponding {@link ArithmeticOpCode} to the instruction list.
 * </p>
 * <p>
 * This class is intended for internal use by {@link VMProgramBuilder}.
 * </p>
 */
public final class ArithmeticBuilder {

  private final InstructionEmitter emitter;

  /**
   * Creates a new arithmetic builder with the given instruction emitter.
   *
   * @param emitter the instruction emitter used to append arithmetic operations
   */
  ArithmeticBuilder(InstructionEmitter emitter) {
    this.emitter = emitter;
  }

  /**
   * Emits an ADD instruction to add the top two values on the stack.
   */
  public void add() {
    emitter.emit(ArithmeticOpCode.ADD);
  }

  /**
   * Emits a SUB instruction to subtract the top value from the second top value on the stack.
   */
  public void sub() {
    emitter.emit(ArithmeticOpCode.SUB);
  }

  /**
   * Emits a MUL instruction to multiply the top two values on the stack.
   */
  public void mul() {
    emitter.emit(ArithmeticOpCode.MUL);
  }

  /**
   * Emits a DIV instruction to divide the second top value by the top value on the stack.
   * <p>
   * Throws a runtime error in the VM if the divisor is zero.
   * </p>
   */
  public void div() {
    emitter.emit(ArithmeticOpCode.DIV);
  }

  /**
   * Emits a MOD instruction to compute the modulo of the second top value by the top value on the stack.
   * <p>
   * Throws a runtime error in the VM if the divisor is zero.
   * </p>
   */
  public void mod() {
    emitter.emit(ArithmeticOpCode.MOD);
  }
}