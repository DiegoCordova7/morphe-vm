package dev.eidos.vm.exception.execution;

public final class ExpectedIntegerValueException extends VMExecutionException {
  public ExpectedIntegerValueException(String opcode) {
    super(opcode + " requires an integer value");
  }
}