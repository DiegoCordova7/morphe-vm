package dev.eidos.vm.core.types;

/**
 * Represents a boolean value in the VM.
 * <p>
 * VMBoolean implements {@link IVMValue} and can be stored in the VMHeap or used
 * in VMStack operations. Provides basic value access and string representation.
 * </p>
 */
public final class VMBoolean implements IVMValue {

  private final boolean value;

  /**
   * Constructs a new VMBoolean with the given value.
   *
   * @param value The boolean value to store.
   */
  public VMBoolean(boolean value) {
    this.value = value;
  }

  /**
   * Returns the stored boolean value.
   *
   * @return The boolean value.
   */
  public boolean getValue() {
    return value;
  }

  /**
   * Returns the string representation of this VMBoolean.
   *
   * @return The string form of the boolean value ("true" or "false").
   */
  @Override
  public String toString() {
    return Boolean.toString(value);
  }
}