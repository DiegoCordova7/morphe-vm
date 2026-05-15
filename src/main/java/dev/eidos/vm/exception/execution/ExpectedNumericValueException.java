package dev.eidos.vm.exception.execution;

public final class ExpectedNumericValueException extends VMExecutionException {

  public ExpectedNumericValueException(Object value) {
    super("Expected numeric value, got " +
        (value == null ? "null" : value.getClass().getSimpleName()));
  }
}