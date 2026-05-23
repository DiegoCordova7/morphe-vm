package dev.morphe.vm.exception.builder;

public final class UndefinedLabelException extends VMBuilderException {

  public UndefinedLabelException(String label) {
    super("Undefined label: " + label);
  }
}