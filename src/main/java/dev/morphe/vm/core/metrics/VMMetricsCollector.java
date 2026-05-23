package dev.morphe.vm.core.metrics;

/**
 * Mutable collector used internally by the VM to track execution metrics.
 * <p>
 * The collector is updated incrementally while the VM executes instructions
 * and produces an immutable {@link VMMetrics} snapshot once execution
 * completes.
 * </p>
 *
 * <p>
 * This class is intended for internal runtime instrumentation only.
 * </p>
 */
public final class VMMetricsCollector {

  /** Total number of executed instructions. */
  private long executedInstructions;

  /** Total number of instructions loaded in the program. */
  private int instructionCount;

  /** Highest stack size reached during execution. */
  private int maxStackSize;

  /** Final stack size after execution completes. */
  private int finalStackSize;

  /** Total configured heap capacity. */
  private int heapCapacity;

  /** Current number of allocated heap slots. */
  private int usedHeapSlots;

  /** Highest number of allocated heap slots reached during execution. */
  private int peakHeapUsage;

  /** Total VM runtime in nanoseconds. */
  private long runtimeNs;

  /**
   * Sets the total number of instructions loaded in the VM program.
   *
   * @param instructionCount total instruction count
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
   * Records the current stack size.
   * <p>
   * Updates both the final stack size and the maximum stack size reached
   * during execution.
   * </p>
   *
   * @param stackSize current VM stack size
   */
  public void recordStackSize(int stackSize) {
    if (stackSize > maxStackSize) {
      maxStackSize = stackSize;
    }

    finalStackSize = stackSize;
  }

  /**
   * Records the total heap capacity configured for the VM.
   *
   * @param heapCapacity heap capacity
   */
  public void recordHeapCapacity(int heapCapacity) {
    this.heapCapacity = heapCapacity;
  }

  /**
   * Records the current number of allocated heap slots.
   *
   * @param usedHeapSlots used heap slot count
   */
  public void recordUsedHeapSlots(int usedHeapSlots) {
    this.usedHeapSlots = usedHeapSlots;
  }

  /**
   * Records the highest heap usage reached during execution.
   *
   * @param peakHeapUsage peak heap usage
   */
  public void recordPeakHeapUsage(int peakHeapUsage) {
    this.peakHeapUsage = peakHeapUsage;
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
    return new VMMetrics(executedInstructions, instructionCount, maxStackSize,
        finalStackSize, heapCapacity, usedHeapSlots, peakHeapUsage, runtimeNs);
  }
}