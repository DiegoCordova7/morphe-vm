package dev.morphe.vm.exception;

import dev.morphe.common.exception.MorpheException;

/**
	* Exception thrown when a runtime error occurs in the VM.
	*/
public abstract class VMException extends MorpheException {

	public VMException(String message) {
		super(message);
	}

	public VMException(String message, Throwable cause) {
		super(message, cause);
	}
}