package dev.eidos.vm.exception.execution;

public final class ExpectedBooleanValueException extends VMExecutionException {

  public ExpectedBooleanValueException(String opcode) {
    super(opcode + " requires boolean operand(s)");
  }
}