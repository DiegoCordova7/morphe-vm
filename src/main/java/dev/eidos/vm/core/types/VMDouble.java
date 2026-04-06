package dev.eidos.vm.core.types;

/**
	* Represents a double-precision floating-point value in the VM.
	* <p>
	* VMDouble implements {@link IVMValue} and can be stored in the VMHeap or used
	* in VMStack operations. Provides basic value access, string representation, and comparison.
	* </p>
	*/
public final class VMDouble implements IVMValue {

	private final double value;

	/**
		* Constructs a new VMDouble with the given value.
		*
		* @param value The double value to store.
		*/
	public VMDouble(double value) {
		this.value = value;
	}

	/**
		* Returns the stored double value.
		*
		* @return The double value.
		*/
	public double getValue() {
		return value;
	}

	/**
		* Returns the string representation of this VMDouble.
		*
		* @return The string form of the value.
		*/
	@Override
	public String toString() {
		return Double.toString(value);
	}

	/**
		* Compares this VMDouble with another object for equality.
		*
		* @param o The object to compare with.
		* @return True if the other object is a VMDouble with the same value, false otherwise.
		*/
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof VMDouble other)) return false;
		return Double.compare(this.value, other.value) == 0;
	}
}