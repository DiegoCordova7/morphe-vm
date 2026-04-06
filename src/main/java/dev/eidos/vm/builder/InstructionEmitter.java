package dev.eidos.vm.builder;

import dev.eidos.vm.instructions.IOpCodeAction;
import dev.eidos.vm.instructions.Instruction;

/**
 * Utility class responsible for emitting instructions into a program.
 * <p>
 * Wraps a {@link ProgramState} and provides a simple interface for adding
 * new instructions with their corresponding operands.
 * </p>
 * <p>
 * Typically used by the {@link VMProgramBuilder} to append instructions
 * during the compilation or building phase.
 * </p>
 */
final class InstructionEmitter {

  /** The program state where instructions are being collected. */
  private final ProgramState state;

  /**
   * Creates a new InstructionEmitter for the given program state.
   *
   * @param state the program state where instructions will be emitted
   */
  InstructionEmitter(ProgramState state){
    this.state = state;
  }

  /**
   * Adds a new instruction with the specified opcode and operands to the program.
   *
   * @param op the opcode action to execute
   * @param operands zero or more operand indices for the instruction
   */
  void emit(IOpCodeAction op, int... operands){
    state.instructions.add(new Instruction(op, operands));
  }
}