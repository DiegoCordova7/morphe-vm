package dev.eidos.vm.core.types;

/**
	* Represents an integer value in the VM.
	* <p>
	* VMInteger implements {@link IVMValue} and can be stored in the VMHeap or used
	* in VMStack operations. It provides basic value access and comparison.
	* </p>
	*/
public final class VMInteger implements IVMValue {

	private final int value;

	/**
		* Constructs a new VMInteger with the given value.
		*
		* @param value The integer value to store.
		*/
	public VMInteger(int value) {
		this.value = value;
	}

	/**
		* Returns the integer value.
		*
		* @return The stored integer.
		*/
	public int getValue() {
		return value;
	}

	/**
		* Returns the string representation of the integer value.
		*
		* @return The string form of the value.
		*/
	@Override
	public String toString() {
		return Integer.toString(value);
	}

	/**
		* Compares this VMInteger with another object for equality.
		*
		* @param o The object to compare with.
		* @return True if the other object is a VMInteger with the same value, false otherwise.
		*/
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof VMInteger other)) return false;
		return this.value == other.value;
	}
}