package dev.eidos.vm.exception.execution;

import dev.eidos.vm.core.types.IVMValue;

public final class InvalidComparisonException extends VMExecutionException {

  public InvalidComparisonException(String operator, IVMValue a, IVMValue b) {
    super("Invalid comparison for operator " + operator + ": "
        + a.getClass().getSimpleName() + " and "
        + b.getClass().getSimpleName());
  }
}