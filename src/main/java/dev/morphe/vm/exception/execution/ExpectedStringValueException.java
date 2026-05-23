package dev.morphe.vm.exception.execution;

public final class ExpectedStringValueException extends VMExecutionException {

  public ExpectedStringValueException(String opcode) {
    super(opcode + " requires a string value");
  }
}