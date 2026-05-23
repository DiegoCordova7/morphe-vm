package dev.morphe.vm.exception.scope;

public final class GlobalScopeExitException extends VMScopeException {

  public GlobalScopeExitException() {
    super("Cannot exit the global scope");
  }
}