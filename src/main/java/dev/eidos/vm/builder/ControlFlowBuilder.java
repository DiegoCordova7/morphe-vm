package dev.eidos.vm.builder;

import dev.eidos.vm.instructions.opcodes.*;
import dev.eidos.vm.core.types.IVMValue;
import java.util.List;

/**
 * Builder for emitting control flow instructions in the VM program.
 * <p>
 * Supports common control structures: if-else, while, do-while, for loops, and match-case.
 * Provides utility methods for labels, jumps, and halting the VM.
 * Intended for internal use by {@link VMProgramBuilder}.
 * </p>
 */
public final class ControlFlowBuilder {

  private final InstructionEmitter emitter;
  private final ProgramState state;

  /**
   * Creates a new control flow builder.
   *
   * @param emitter the instruction emitter used to append control flow operations
   * @param state   the program state used to manage labels and unresolved jumps
   */
  public ControlFlowBuilder(InstructionEmitter emitter, ProgramState state) {
    this.emitter = emitter;
    this.state = state;
  }

  /**
   * Emits a HALT instruction that stops VM execution.
   */
  public void halt() {
    emitter.emit(ControlOpCode.HALT);
  }

  /**
   * Emits an if-else construct.
   *
   * @param condition the condition block that evaluates to a boolean on the stack
   * @param ifBlock   the block to execute if the condition is true
   * @param elseBlock optional block to execute if the condition is false
   */
  public void ifElse(Runnable condition, Runnable ifBlock, Runnable... elseBlock) {
    String elseLabel = newLabel("else");
    String endLabel = newLabel("ifend");
    condition.run();
    jmpIfFalse(elseLabel);
    ifBlock.run();
    jmp(endLabel);
    label(elseLabel);
    if (elseBlock.length > 0) elseBlock[0].run();
    label(endLabel);
  }

  /**
   * Emits a while loop.
   * <p>
   * Execution flow:
   * <ul>
   *   <li>Evaluates the condition</li>
   *   <li>If false, jumps to the end</li>
   *   <li>Executes the body</li>
   *   <li>Jumps back to the condition</li>
   * </ul>
   *
   * @param condition the loop condition (must leave a boolean on the stack)
   * @param body      the loop body
   */
  public void whileLoop(Runnable condition, Runnable body) {
    String startLabel = newLabel("while_start");
    String endLabel = newLabel("while_end");
    label(startLabel);
    condition.run();
    jmpIfFalse(endLabel);
    body.run();
    jmp(startLabel);
    label(endLabel);
  }

  /**
   * Emits a do-while loop.
   * <p>
   * Execution flow:
   * <ul>
   *   <li>Executes the body</li>
   *   <li>Evaluates the condition</li>
   *   <li>If true, repeats</li>
   * </ul>
   *
   * @param body      the loop body
   * @param condition the loop condition (must leave a boolean on the stack)
   */
  public void doWhileLoop(Runnable body, Runnable condition) {
    String startLabel = newLabel("do_start");
    String endLabel = newLabel("do_end");
    label(startLabel);
    body.run();
    condition.run();
    jmpIfTrue(startLabel);
    label(endLabel);
  }

  /**
   * Emits a for loop.
   * <p>
   * Execution flow:
   * <ul>
   *   <li>Executes initialization (if present)</li>
   *   <li>Evaluates condition (if present)</li>
   *   <li>If false, exits loop</li>
   *   <li>Executes body</li>
   *   <li>Executes increment (if present)</li>
   *   <li>Repeats</li>
   * </ul>
   *
   * @param init      optional initialization block
   * @param condition optional condition block (must leave a boolean on the stack)
   * @param increment optional increment block
   * @param body      the loop body
   */
  public void forLoop(Runnable init, Runnable condition, Runnable increment, Runnable body) {
    if (init != null) init.run();
    String startLabel = newLabel("for_start");
    String endLabel = newLabel("for_end");
    label(startLabel);
    if (condition != null) {
      condition.run();
      jmpIfFalse(endLabel);
    }
    body.run();
    if (increment != null) increment.run();
    jmp(startLabel);
    label(endLabel);
  }

  /**
   * Emits a match-case structure.
   *
   * @param expr     the expression to evaluate
   * @param consumer a consumer that receives a {@link MatchBuilder} to define cases
   */
  public void match(Runnable expr, MatchConsumer consumer) {
    expr.run();
    int exprIndex = state.heap.allocTemp();
    emitter.emit(StackOpCode.POP_TO_HEAP, exprIndex);
    String endLabel = newLabel("match_end");
    MatchBuilder builder = new MatchBuilder(emitter, state, endLabel, exprIndex);
    consumer.accept(builder);
    label(endLabel);
  }

  /**
   * Functional interface used to define match-case branches.
   */
  public interface MatchConsumer {
    /**
     * Accepts a {@link MatchBuilder} to define match cases.
     *
     * @param m the match builder
     */
    void accept(MatchBuilder m);
  }

  /**
   * Creates a new match builder.
   */
  public static class MatchBuilder {

    private final InstructionEmitter emitter;
    private final ProgramState state;
    private final String endLabel;
    private final int exprIndex;

    /**
     * Builder for defining match-case branches.
     * <p>
     * Each case compares the evaluated expression against one or more values.
     * If a case matches, its block is executed and control jumps to the end.
     * </p>
     */
    MatchBuilder(InstructionEmitter emitter, ProgramState state, String endLabel, int exprIndex) {
      this.emitter = emitter;
      this.state = state;
      this.endLabel = endLabel;
      this.exprIndex = exprIndex;
    }

    /**
     * Matches a single value using equality (==).
     *
     * @param value the value to compare against
     * @param block the block to execute if matched
     * @return this builder for chaining
     */
    public MatchBuilder caseVal(IVMValue value, Runnable block) {
      String nextCase = newLabel("case");
      int valueIndex = state.heap.alloc(value);
      emitter.emit(StackOpCode.LOAD_HEAP, exprIndex);
      emitter.emit(StackOpCode.LOAD_HEAP, valueIndex);
      emitter.emit(ComparisonOpCode.EQ);
      jmpIfFalse(nextCase);
      block.run();
      jmp(endLabel);
      label(nextCase);
      return this;
    }

    /**
     * Matches a value within a range [start, end].
     * <p>
     * Internally evaluates:
     * (expr &gt;= start) AND (expr &lt;= end)
     * </p>
     *
     * @param start lower bound (inclusive)
     * @param end   upper bound (inclusive)
     * @param block the block to execute if matched
     * @return this builder for chaining
     */
    public MatchBuilder caseRange(IVMValue start, IVMValue end, Runnable block) {
      String nextCase = newLabel("case");
      int startIndex = state.heap.alloc(start);
      int endIndex = state.heap.alloc(end);
      emitter.emit(StackOpCode.LOAD_HEAP, exprIndex);
      emitter.emit(StackOpCode.LOAD_HEAP, startIndex);
      emitter.emit(ComparisonOpCode.GTE);
      int gteResult = state.heap.allocTemp();
      emitter.emit(StackOpCode.POP_TO_HEAP, gteResult);
      emitter.emit(StackOpCode.LOAD_HEAP, exprIndex);
      emitter.emit(StackOpCode.LOAD_HEAP, endIndex);
      emitter.emit(ComparisonOpCode.LTE);
      int lteResult = state.heap.allocTemp();
      emitter.emit(StackOpCode.POP_TO_HEAP, lteResult);
      emitter.emit(StackOpCode.LOAD_HEAP, gteResult);
      emitter.emit(StackOpCode.LOAD_HEAP, lteResult);
      emitter.emit(LogicalOpCode.AND);
      jmpIfFalse(nextCase);
      block.run();
      jmp(endLabel);
      label(nextCase);
      return this;
    }

    /**
     * Matches if the expression equals any value in the list.
     * <p>
     * Internally builds a chain of OR comparisons.
     * </p>
     *
     * @param values list of possible matching values
     * @param block  the block to execute if any match
     * @return this builder for chaining
     */
    public MatchBuilder caseList(List<IVMValue> values, Runnable block) {
      String nextCase = newLabel("case");
      boolean first = true;
      for (IVMValue value : values) {
        int valueIndex = state.heap.alloc(value);
        emitter.emit(StackOpCode.LOAD_HEAP, exprIndex);
        emitter.emit(StackOpCode.LOAD_HEAP, valueIndex);
        emitter.emit(ComparisonOpCode.EQ);
        if (!first) emitter.emit(LogicalOpCode.OR);
        first = false;
      }
      jmpIfFalse(nextCase);
      block.run();
      jmp(endLabel);
      label(nextCase);
      return this;
    }

    /**
     * Defines the default case executed if no other case matches.
     *
     * @param block the default block
     */
    public void defaultCase(Runnable block) {
      block.run();
    }

    /**
     * Registers a label at the current instruction index.
     *
     * @param name the label name
     */
    private void label(String name) {
      state.labels.put(name, state.instructions.size());
    }

    /**
     * Generates a unique label name with a given prefix.
     *
     * @param prefix label prefix
     * @return unique label name
     */
    private String newLabel(String prefix) {
      return prefix + "_" + (state.labelCounter++);
    }

    /**
     * Emits an unconditional jump to a label.
     * The target is resolved later during program build.
     *
     * @param label the target label
     */
    private void jmp(String label) {
      int index = state.instructions.size();
      emitter.emit(ControlOpCode.JMP, -1);
      state.unresolvedJumps.put(index, label);
    }

    /**
     * Emits a conditional jump if the top of the stack is false.
     *
     * @param label the target label
     */
    private void jmpIfFalse(String label) {
      int index = state.instructions.size();
      emitter.emit(ControlOpCode.JMP_IF_FALSE, -1);
      state.unresolvedJumps.put(index, label);
    }
  }

  /**
   * Registers a label at the current instruction index.
   *
   * @param name the label name
   */
  private void label(String name) {
    state.labels.put(name, state.instructions.size());
  }

  /**
   * Generates a unique label name with a given prefix.
   *
   * @param prefix label prefix
   * @return unique label name
   */
  private String newLabel(String prefix) {
    return prefix + "_" + (state.labelCounter++);
  }

  /**
   * Emits an unconditional jump to a label.
   * The target is resolved later during program build.
   *
   * @param label the target label
   */
  private void jmp(String label) {
    int index = state.instructions.size();
    emitter.emit(ControlOpCode.JMP, -1);
    state.unresolvedJumps.put(index, label);
  }

  /**
   * Emits a conditional jump if the top of the stack is false.
   *
   * @param label the target label
   */
  private void jmpIfFalse(String label) {
    int index = state.instructions.size();
    emitter.emit(ControlOpCode.JMP_IF_FALSE, -1);
    state.unresolvedJumps.put(index, label);
  }

  /**
   * Emits a conditional jump if the top of the stack is true.
   *
   * @param label the target label
   */
  private void jmpIfTrue(String label) {
    int index = state.instructions.size();
    emitter.emit(ControlOpCode.JMP_IF_TRUE, -1);
    state.unresolvedJumps.put(index, label);
  }
}