package dev.eidos.vm.builder;

import dev.eidos.vm.core.types.VMString;
import dev.eidos.vm.instructions.opcodes.ScopeOpCode;

/**
 * Builder for emitting scope-related instructions in the VM program.
 * <p>
 * Supports storing and loading variables, as well as entering and exiting scopes.
 * Intended for internal use by {@link VMProgramBuilder}.
 * </p>
 */
public final class ScopeBuilder {

  private final InstructionEmitter emitter;
  private final ProgramState state;

  /**
   * Creates a new scope builder.
   *
   * @param emitter the instruction emitter used to append scope operations
   * @param state the current program state
   */
  ScopeBuilder(InstructionEmitter emitter, ProgramState state) {
    this.emitter = emitter;
    this.state = state;
  }

  /**
   * Returns the heap index of a variable name, creating a new VMString if needed.
   *
   * @param name the variable name
   * @return the heap index corresponding to the name
   */
  private int identifier(String name) {
    if (state.identifierPool.containsKey(name))
      return state.identifierPool.get(name);
    int index = state.heap.alloc(new VMString(name));
    state.identifierPool.put(name, index);
    return index;
  }

  /**
   * Emits a STORE instruction to store the top of the stack into the given variable name.
   *
   * @param name the variable name
   */
  public void store(String name) {
    int nameIndex = identifier(name);
    emitter.emit(ScopeOpCode.STORE, nameIndex);
  }

  /**
   * Emits a LOAD instruction to push the value of the given variable name onto the stack.
   *
   * @param name the variable name
   */
  public void load(String name) {
    int nameIndex = identifier(name);
    emitter.emit(ScopeOpCode.LOAD, nameIndex);
  }

  /** Emits an ENTER_SCOPE instruction to create a new variable scope. */
  public void enterScope() {
    emitter.emit(ScopeOpCode.ENTER_SCOPE);
  }

  /** Emits an EXIT_SCOPE instruction to exit the current variable scope. */
  public void exitScope() {
    emitter.emit(ScopeOpCode.EXIT_SCOPE);
  }
}