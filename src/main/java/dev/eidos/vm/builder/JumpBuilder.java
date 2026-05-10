package dev.eidos.vm.builder;

import dev.eidos.vm.instructions.opcodes.ControlOpCode;

/**
 * Utility builder for emitting jump and label instructions in a VM program.
 * <p>
 * {@code JumpBuilder} centralizes low-level control-flow operations such as:
 * <ul>
 *   <li>creating unique labels</li>
 *   <li>registering labels at the current instruction index</li>
 *   <li>emitting unconditional jumps</li>
 *   <li>emitting conditional jumps</li>
 * </ul>
 *
 * <p>
 * Jump targets are emitted temporarily using {@code -1} and resolved later
 * when the final VM program is built.
 */
public final class JumpBuilder {

  private final InstructionEmitter emitter;
  private final ProgramState state;

  /**
   * Creates a new jump builder.
   *
   * @param emitter the instruction emitter used to append jump instructions
   * @param state the program state used to track labels and unresolved jumps
   */
  public JumpBuilder(InstructionEmitter emitter, ProgramState state) {
    this.emitter = emitter;
    this.state = state;
  }

  /**
   * Registers a label at the current instruction index.
   *
   * @param name the label name
   */
  public void label(String name) {
    state.labels.put(name, state.instructions.size());
  }

  /**
   * Generates a unique label name using the given prefix.
   *
   * @param prefix the label prefix
   * @return a unique label name
   */
  public String newLabel(String prefix) {
    return prefix + "_" + (state.labelCounter++);
  }

  /**
   * Emits an unconditional jump to a label.
   * <p>
   * The target address is resolved later during program building.
   *
   * @param label the target label
   */
  public void jmp(String label) {
    int index = state.instructions.size();
    emitter.emit(ControlOpCode.JMP, -1);
    state.unresolvedJumps.put(index, label);
  }

  /**
   * Emits a conditional jump if the top stack value is false.
   * <p>
   * The target address is resolved later during program building.
   *
   * @param label the target label
   */
  public void jmpIfFalse(String label) {
    int index = state.instructions.size();
    emitter.emit(ControlOpCode.JMP_IF_FALSE, -1);
    state.unresolvedJumps.put(index, label);
  }

  /**
   * Emits a conditional jump if the top stack value is true.
   * <p>
   * The target address is resolved later during program building.
   *
   * @param label the target label
   */
  public void jmpIfTrue(String label) {
    int index = state.instructions.size();
    emitter.emit(ControlOpCode.JMP_IF_TRUE, -1);
    state.unresolvedJumps.put(index, label);
  }
}