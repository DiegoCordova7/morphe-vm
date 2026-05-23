package dev.morphe.vm.core.types;

/**
 * Represents a value that can be stored in the VMHeap.
 * <p>
 * All types used by the VM (integers, strings, objects, etc.) should implement this interface.
 * It serves as a common type for heap storage, stack operations, and instruction execution.
 * </p>
 */
public interface IVMValue {}