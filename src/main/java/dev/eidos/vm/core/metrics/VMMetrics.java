package dev.eidos.vm.core.metrics;

/**
 * Stores runtime metrics collected during VM execution.
 * <p>
 * These metrics describe how the VM behaved while executing a program.
 * They are intended for diagnostics, profiling, monitoring, and debugging.
 * </p>
 */
public final class VMMetrics {

  private final long executedInstructions;
  private final int instructionCount;
  private final int maxStackSize;
  private final int finalStackSize;
  private final int heapSize;
  private final long runtimeNs;

  public VMMetrics(long executedInstructions, int instructionCount, int maxStackSize,
                   int finalStackSize, int heapSize, long runtimeNs) {
    this.executedInstructions = executedInstructions;
    this.instructionCount = instructionCount;
    this.maxStackSize = maxStackSize;
    this.finalStackSize = finalStackSize;
    this.heapSize = heapSize;
    this.runtimeNs = runtimeNs;
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

  public int getHeapSize() {
    return heapSize;
  }

  public long getRuntimeNs() {
    return runtimeNs;
  }
}