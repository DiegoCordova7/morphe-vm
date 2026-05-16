package dev.eidos.vm.exception;

import dev.eidos.common.exception.EidosException;

/**
	* Exception thrown when a runtime error occurs in the VM.
	*/
public abstract class VMException extends EidosException {

	public VMException(String message) {
		super(message);
	}

	public VMException(String message, Throwable cause) {
		super(message, cause);
	}
}