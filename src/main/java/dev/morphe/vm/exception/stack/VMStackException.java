package dev.morphe.vm.exception.stack;

import dev.morphe.vm.exception.VMException;

public abstract class VMStackException extends VMException {

  protected VMStackException(String message) {
    super(message);
  }

  protected VMStackException(String message, Throwable cause) {
    super(message, cause);
  }
}