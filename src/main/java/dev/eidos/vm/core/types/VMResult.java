package dev.eidos.vm.core.types;

/**
 * Represents the result of a VM operation, which can either be a value or an error.
 * <p>
 * VMResult implements {@link IVMValue} and provides a standardized way to return
 * either a successful value or an error message.
 * </p>
 */
public final class VMResult implements IVMValue {

  private final IVMValue value;
  private final String error;
  private final boolean isError;

  private VMResult(IVMValue value, String error, boolean isError) {
    this.value = value;
    this.error = error;
    this.isError = isError;
  }

  /**
   * Creates a successful result containing the given value.
   *
   * @param value The value of the result.
   * @return A VMResult representing success.
   */
  public static VMResult ok(IVMValue value) {
    return new VMResult(value, null, false);
  }

  /**
   * Creates an error result with the given error message.
   *
   * @param error The error message.
   * @return A VMResult representing an error.
   */
  public static VMResult error(String error) {
    return new VMResult(null, error, true);
  }

  /**
   * Checks if this result represents an error.
   *
   * @return True if this is an error result, false otherwise.
   */
  public boolean isError() {
    return isError;
  }

  /**
   * Returns the value contained in this result.
   *
   * @return The IVMValue if successful, or null if it is an error.
   */
  public IVMValue getValue() {
    return value;
  }

  /**
   * Returns the error message contained in this result.
   *
   * @return The error string if it is an error, or null if successful.
   */
  public String getError() {
    return error;
  }
}