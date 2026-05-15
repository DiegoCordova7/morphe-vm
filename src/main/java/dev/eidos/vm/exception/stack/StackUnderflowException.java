package dev.eidos.vm.exception.stack;

public final class StackUnderflowException extends VMStackException {

  public StackUnderflowException() {
    super("Stack underflow");
  }
}