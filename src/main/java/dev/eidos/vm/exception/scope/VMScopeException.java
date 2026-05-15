package dev.eidos.vm.exception.scope;

import dev.eidos.vm.exception.VMException;

public abstract class VMScopeException extends VMException {

  protected VMScopeException(String message) {
    super(message);
  }

  protected VMScopeException(String message, Throwable cause) {
    super(message, cause);
  }
}