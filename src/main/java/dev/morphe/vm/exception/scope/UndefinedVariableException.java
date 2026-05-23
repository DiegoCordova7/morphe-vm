package dev.morphe.vm.exception.scope;

public final class UndefinedVariableException extends VMScopeException {

  public UndefinedVariableException(String name) {
    super("Variable not defined: " + name);
  }
}