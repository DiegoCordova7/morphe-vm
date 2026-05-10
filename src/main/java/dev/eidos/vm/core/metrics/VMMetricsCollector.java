package dev.eidos.vm.core.metrics;

/**
 * Mutable collector used internally by the VM to track execution metrics.
 * <p>
 * The collector is updated while the VM runs and produces an immutable
 * {@link VMMetrics} snapshot when execution finishes.
 * </p>
 */
public final class VMMetricsCollector {

  private long executedInstructions;
  private int instructionCount;
  private int maxStackSize;
  private int finalStackSize;
  private int heapSize;
  private long runtimeNs;

  /**
   * Sets the total number of instructions loaded in the VM program.
   *
   * @param instructionCount the total instruction count
   */
  public void setInstructionCount(int instructionCount) {
    this.instructionCount = instructionCount;
  }

  /**
   * Records the execution of one instruction.
   */
  public void recordInstructionExecution() {
    executedInstructions++;
  }

  /**
   * Records the current stack size and updates the maximum stack size
   * if the current value is higher.
   *
   * @param stackSize the current VM stack size
   */
  public void recordStackSize(int stackSize) {
    if (stackSize > maxStackSize) {
      maxStackSize = stackSize;
    }

    finalStackSize = stackSize;
  }

  /**
   * Records the final heap size.
   *
   * @param heapSize the amount of values stored in the heap
   */
  public void recordHeapSize(int heapSize) {
    this.heapSize = heapSize;
  }

  /**
   * Records the total VM runtime.
   *
   * @param runtimeNs execution time in nanoseconds
   */
  public void recordRuntime(long runtimeNs) {
    this.runtimeNs = runtimeNs;
  }

  /**
   * Builds an immutable metrics snapshot.
   *
   * @return collected VM metrics
   */
  public VMMetrics snapshot() {
    return new VMMetrics(executedInstructions, instructionCount, maxStackSize, finalStackSize, heapSize, runtimeNs);
  }
}