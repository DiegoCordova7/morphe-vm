package dev.morphe.vm.exception.scope;

import dev.morphe.vm.exception.VMException;

public abstract class VMScopeException extends VMException {

  protected VMScopeException(String message) {
    super(message);
  }

  protected VMScopeException(String message, Throwable cause) {
    super(message, cause);
  }
}