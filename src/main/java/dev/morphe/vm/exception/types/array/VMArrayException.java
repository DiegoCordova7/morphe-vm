package dev.morphe.vm.exception.types.array;

import dev.morphe.vm.exception.VMException;

public abstract class VMArrayException extends VMException {

  protected VMArrayException(String message) {
    super(message);
  }

  protected VMArrayException(String message, Throwable cause) {
    super(message, cause);
  }
}