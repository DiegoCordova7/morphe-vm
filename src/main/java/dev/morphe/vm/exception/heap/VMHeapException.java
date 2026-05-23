package dev.morphe.vm.exception.heap;

import dev.morphe.vm.exception.VMException;

public abstract class VMHeapException extends VMException {

  protected VMHeapException(String message) {
    super(message);
  }

  protected VMHeapException(String message, Throwable cause) {
    super(message, cause);
  }
}