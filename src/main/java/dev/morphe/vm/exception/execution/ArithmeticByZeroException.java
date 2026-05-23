package dev.morphe.vm.exception.execution;

public final class ArithmeticByZeroException extends VMExecutionException {

  public ArithmeticByZeroException(String opcode) {
    super(opcode + " by zero");
  }
}