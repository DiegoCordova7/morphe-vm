package dev.morphe.vm.exception.execution;

public final class InvalidOperandException extends VMExecutionException {

  public InvalidOperandException(String opcode) {
    super("Invalid operands for " + opcode);
  }
}