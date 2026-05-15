package dev.eidos.vm.exception.builder;

public final class UnsupportedLiteralException extends VMBuilderException {

  public UnsupportedLiteralException(Object value) {
    super("Unsupported literal: " + value + " (" +
        (value == null ? "null" : value.getClass().getSimpleName()) + ")");
  }
}