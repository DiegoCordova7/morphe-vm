package dev.eidos.vm.core.metrics;

/**
 * Immutable snapshot of metrics collected during a VM execution.
 * <p>
 * {@code VMMetrics} contains low-level runtime information describing
 * how the virtual machine behaved while executing a program.
 * These metrics can be used for:
 * </p>
 *
 * <ul>
 *   <li>Performance profiling</li>
 *   <li>Execution diagnostics</li>
 *   <li>Runtime monitoring</li>
 *   <li>Debugging and analysis</li>
 *   <li>External observability systems (e.g. Prometheus/Grafana)</li>
 * </ul>
 *
 * <p>
 * Metrics are collected by the VM runtime and exposed as a final immutable
 * result once execution completes.
 * </p>
 */
public final class VMMetrics {

  /**
   * Total number of instructions executed by the VM.
   * <p>
   * Unlike {@link #instructionCount}, this value includes repeated
   * execution caused by loops, recursion, and control flow.
   * </p>
   */
  private final long executedInstructions;

  /**
   * Total number of instructions contained in the loaded program.
   */
  private final int instructionCount;

  /**
   * Highest stack size reached during execution.
   */
  private final int maxStackSize;

  /**
   * Final stack size after execution completed.
   */
  private final int finalStackSize;

  /**
   * Total heap capacity available to the VM.
   */
  private final int heapSize;

  /**
   * Total execution time in nanoseconds.
   */
  private final long runtimeNs;

  /**
   * Creates a new immutable metrics snapshot.
   *
   * @param executedInstructions total executed instructions
   * @param instructionCount total instructions in the program
   * @param maxStackSize maximum stack size reached
   * @param finalStackSize final stack size after execution
   * @param heapSize configured heap size
   * @param runtimeNs total execution time in nanoseconds
   */
  public VMMetrics(long executedInstructions, int instructionCount, int maxStackSize,
                   int finalStackSize, int heapSize, long runtimeNs) {
    this.executedInstructions = executedInstructions;
    this.instructionCount = instructionCount;
    this.maxStackSize = maxStackSize;
    this.finalStackSize = finalStackSize;
    this.heapSize = heapSize;
    this.runtimeNs = runtimeNs;
  }

  /**
   * Returns the total number of executed instructions.
   *
   * @return executed instruction count
   */
  public long getExecutedInstructions() {
    return executedInstructions;
  }

  /**
   * Returns the total number of instructions in the program.
   *
   * @return static instruction count
   */
  public int getInstructionCount() {
    return instructionCount;
  }

  /**
   * Returns the maximum stack size reached during execution.
   *
   * @return maximum stack size
   */
  public int getMaxStackSize() {
    return maxStackSize;
  }

  /**
   * Returns the final stack size after execution.
   *
   * @return final stack size
   */
  public int getFinalStackSize() {
    return finalStackSize;
  }

  /**
   * Returns the configured heap capacity.
   *
   * @return heap size
   */
  public int getHeapSize() {
    return heapSize;
  }

  /**
   * Returns the total runtime in nanoseconds.
   *
   * @return runtime duration
   */
  public long getRuntimeNs() {
    return runtimeNs;
  }
}