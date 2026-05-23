package dev.morphe.vm.exception.execution;

public final class ExpectedArrayValueException extends VMExecutionException {
  public ExpectedArrayValueException(String opcode) {
    super(opcode + " requires an array value");
  }
}