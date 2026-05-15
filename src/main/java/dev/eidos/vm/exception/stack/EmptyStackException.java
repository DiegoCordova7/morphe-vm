package dev.eidos.vm.exception.stack;

public final class EmptyStackException extends VMStackException {

  public EmptyStackException() {
    super("Stack is empty");
  }
}