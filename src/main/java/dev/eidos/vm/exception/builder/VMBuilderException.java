package dev.eidos.vm.exception.builder;

import dev.eidos.vm.exception.VMException;

public abstract class VMBuilderException extends VMException {

  protected VMBuilderException(String message) {
    super(message);
  }

  protected VMBuilderException(String message, Throwable cause) {
    super(message, cause);
  }
}
