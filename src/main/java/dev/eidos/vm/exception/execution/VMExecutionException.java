package dev.eidos.vm.exception.execution;

import dev.eidos.vm.exception.VMException;

public abstract class VMExecutionException extends VMException {

  protected VMExecutionException(String message) {
    super(message);
  }

  protected VMExecutionException(String message, Throwable cause) {
    super(message, cause);
  }
}