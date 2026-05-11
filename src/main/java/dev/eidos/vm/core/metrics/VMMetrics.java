package dev.eidos.vm.core.metrics;

/**
 * Immutable snapshot of metrics collected during a VM execution.
 * <p>
 * {@code VMMetrics} contains low-level runtime information describing how the
 * virtual machine behaved while executing a program.
 * </p>
 */
public final class VMMetrics {

  /** Total number of instructions executed by the VM. */
  private final long executedInstructions;

  /** Total number of instructions contained in the loaded program. */
  private final int instructionCount;

  /** Highest stack size reached during execution. */
  private final int maxStackSize;

  /** Final stack size after execution completed. */
  private final int finalStackSize;

  /** Total heap capacity configured for the VM. */
  private final int heapCapacity;

  /** Number of heap slots still allocated when execution completed. */
  private final int usedHeapSlots;

  /** Highest number of allocated heap slots reached during execution. */
  private final int peakHeapUsage;

  /** Total execution time in nanoseconds. */
  private final long runtimeNs;

  /** Number of executed instructions per second. */
  private final double instructionsPerSecond;

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

  public long getExecutedInstructions() {
    return executedInstructions;
  }

  public int getInstructionCount() {
    return instructionCount;
  }

  public int getMaxStackSize() {
    return maxStackSize;
  }

  public int getFinalStackSize() {
    return finalStackSize;
  }

  public int getHeapCapacity() {
    return heapCapacity;
  }

  public int getUsedHeapSlots() {
    return usedHeapSlots;
  }

  public int getPeakHeapUsage() {
    return peakHeapUsage;
  }

  public long getRuntimeNs() {
    return runtimeNs;
  }

  public double getInstructionsPerSecond() {
    return instructionsPerSecond;
  }
}