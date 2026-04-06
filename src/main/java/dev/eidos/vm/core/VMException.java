package dev.eidos.vm.core;

/**
	* Custom exception for the VM.
	* <p>
	* Thrown when an error occurs during the execution of a program on the VM,
	* allowing specific runtime issues of the language environment to be identified.
	* </p>
	*/
public class VMException extends RuntimeException {

	/**
		* Constructs a new VMException with a descriptive message.
		*
		* @param message The message describing the error that occurred in the VM.
		*/
	public VMException(String message) {
		super(message);
	}
}