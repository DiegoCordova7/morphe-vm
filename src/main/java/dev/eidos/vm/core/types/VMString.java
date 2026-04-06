package dev.eidos.vm.core.types;

/**
	* Represents a string value in the VM.
	* <p>
	* VMString implements {@link IVMValue} and can be stored in the VMHeap or used
	* in VMStack operations. Provides basic value access, string representation, and comparison.
	* </p>
	*/
public final class VMString implements IVMValue {

	private final String value;

	/**
		* Constructs a new VMString with the given value.
		*
		* @param value The string value to store.
		*/
	public VMString(String value) {
		this.value = value;
	}

	/**
		* Returns the stored string value.
		*
		* @return The string value.
		*/
	public String getValue() {
		return value;
	}

	/**
		* Returns the string representation of this VMString.
		*
		* @return The string value itself.
		*/
	@Override
	public String toString() {
		return value;
	}

	/**
		* Compares this VMString with another object for equality.
		*
		* @param o The object to compare with.
		* @return True if the other object is a VMString with the same value, false otherwise.
		*/
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof VMString other)) return false;
		return this.value.equals(other.value);
	}
}