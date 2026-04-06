package dev.eidos.vm.core;

import java.util.HashMap;
import java.util.Map;

/**
	* Represents a variable scope in the VM.
	* <p>
	* Each VMScope instance holds a mapping of variable names to their heap indices.
	* Scopes can be nested, and variable lookups or assignments will recursively
	* check parent scopes if the variable is not found in the current scope.
	* </p>
	*/
public final class VMScope {

	private final VMScope parent;
	private final Map<String, Integer> vars = new HashMap<>();

	/**
		* Creates a new scope with an optional parent scope.
		*
		* @param parent The parent scope. Can be null for the global scope.
		*/
	public VMScope(VMScope parent) {
		this.parent = parent;
	}

	/**
		* Returns the parent scope.
		*
		* @return The parent VMScope, or null if this is the global scope.
		*/
	public VMScope getParent() {
		return parent;
	}

	/**
		* Declares a new variable in the current scope.
		*
		* @param name The variable name.
		* @param heapIndex The heap index associated with this variable.
		*/
	public void declareVar(String name, int heapIndex) {
		vars.put(name, heapIndex);
	}

	/**
		* Assigns a value to a variable.
		* <p>
		* If the variable exists in the current scope, it is updated.
		* Otherwise, the assignment is recursively attempted in the parent scope.
		* </p>
		*
		* @param name The variable name.
		* @param heapIndex The new heap index for the variable.
		* @return True if the variable was successfully assigned, false if not found.
		*/
	public boolean assignVar(String name, int heapIndex) {
		if (vars.containsKey(name)) {
			vars.put(name, heapIndex);
			return true;
		}
		if (parent != null) return parent.assignVar(name, heapIndex);
		return false;
	}

	/**
		* Retrieves the heap index of a variable.
		* <p>
		* Looks in the current scope first, then recursively in parent scopes.
		* Throws a RuntimeException if the variable is not defined.
		* </p>
		*
		* @param name The variable name.
		* @return The heap index of the variable.
		* @throws VMException if the variable is not defined.
		*/
	public int getVar(String name) {
		if (vars.containsKey(name)) return vars.get(name);
		if (parent != null) return parent.getVar(name);

		throw new VMException("Variable not defined: " + name);
	}
}