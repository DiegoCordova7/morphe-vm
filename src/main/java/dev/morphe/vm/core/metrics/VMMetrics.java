package dev.morphe.vm.core.metrics;

/**
 * Immutable snapshot of metrics collected during a VM execution.
 *
 * <p>
 * {@code VMMetrics} contains low-level runtime information describing how the
 * virtual machine behaved while executing a program.
 * These metrics can be used for:
 * </p>
 *
 * <ul>
 *   <li>Performance profiling</li>
 *   <li>Runtime diagnostics</li>
 *   <li>Execution analysis</li>
 *   <li>Monitoring and observability</li>
 *   <li>External metrics integrations</li>
 * </ul>
 *
 * <p>
 * Metrics are collected incrementally while the VM executes instructions
 * and exposed as an immutable snapshot once execution completes.
 * </p>
 */
public final class VMMetrics {

  /**
   * Total number of instructions executed by the VM.
   *
   * <p>
   * Unlike {@link #instructionCount}, this value includes repeated execution
   * caused by loops, recursion, and control flow.
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
   * Total heap capacity configured for the VM.
   */
  private final int heapCapacity;

  /**
   * Number of heap slots still allocated when execution completed.
   */
  private final int usedHeapSlots;

  /**
   * Highest number of allocated heap slots reached during execution.
   */
  private final int peakHeapUsage;

  /**
   * Total execution time in nanoseconds.
   */
  private final long runtimeNs;

  /**
   * Average number of executed instructions per second.
   */
  private final double instructionsPerSecond;

  /**
   * Creates a new immutable VM metrics snapshot.
   *
   * @param executedInstructions total executed instructions
   * @param instructionCount total instructions loaded in the program
   * @param maxStackSize maximum stack size reached during execution
   * @param finalStackSize final stack size after execution
   * @param heapCapacity total configured heap capacity
   * @param usedHeapSlots allocated heap slots remaining after execution
   * @param peakHeapUsage highest heap usage reached during execution
   * @param runtimeNs total execution time in nanoseconds
   */
  public VMMetrics(long executedInstructions, int instructionCount, int maxStackSize, int finalStackSize,
                   int heapCapacity, int usedHeapSlots, int peakHeapUsage, long runtimeNs) {
    this.executedInstructions = executedInstructions;
    this.instructionCount = instructionCount;
    this.maxStackSize = maxStackSize;
    this.finalStackSize = finalStackSize;
    this.heapCapacity = heapCapacity;
    this.usedHeapSlots = usedHeapSlots;
    this.peakHeapUsage = peakHeapUsage;
    this.runtimeNs = runtimeNs;
    this.instructionsPerSecond = runtimeNs == 0 ? 0.0 : executedInstructions / (runtimeNs / 1_000_000_000.0);
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
   * Returns the total number of instructions contained in the program.
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
   * Returns the final stack size after execution completed.
   *
   * @return final stack size
   */
  public int getFinalStackSize() {
    return finalStackSize;
  }

  /**
   * Returns the total configured heap capacity.
   *
   * @return heap capacity
   */
  public int getHeapCapacity() {
    return heapCapacity;
  }

  /**
   * Returns the number of allocated heap slots remaining after execution.
   *
   * @return used heap slot count
   */
  public int getUsedHeapSlots() {
    return usedHeapSlots;
  }

  /**
   * Returns the highest heap usage reached during execution.
   *
   * @return peak heap usage
   */
  public int getPeakHeapUsage() {
    return peakHeapUsage;
  }

  /**
   * Returns the total execution runtime in nanoseconds.
   *
   * @return runtime duration
   */
  public long getRuntimeNs() {
    return runtimeNs;
  }

  /**
   * Returns the average number of executed instructions per second.
   *
   * @return instruction throughput
   */
  public double getInstructionsPerSecond() {
    return instructionsPerSecond;
  }
}
