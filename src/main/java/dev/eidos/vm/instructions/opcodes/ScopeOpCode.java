package dev.eidos.vm.instructions.opcodes;

import dev.eidos.vm.exception.VMException;
import dev.eidos.vm.core.*;
import dev.eidos.vm.core.types.*;
import dev.eidos.vm.instructions.*;

/**
	* Enum representing scope and variable operations for the VM.
	* <p>
	* Each enum constant implements {@link IOpCodeAction} and defines the behavior
	* of a specific scope-related opcode, including variable storage, loading,
	* and scope management.
	* </p>
	* <p>
	* Supported operations:
	* </p>
	* <ul>
	*   <li>{@link #STORE} - stores the top value of the stack in the current scope under a name from the heap.</li>
	*   <li>{@link #LOAD} - loads a value from the current scope by name and pushes it onto the stack.</li>
	*   <li>{@link #ENTER_SCOPE} - creates a new child scope and sets it as the current scope.</li>
	*   <li>{@link #EXIT_SCOPE} - exits the current scope and returns to the parent scope; throws an exception if attempting to exit the global scope.</li>
	* </ul>
	*/
public enum ScopeOpCode implements IOpCodeAction {

	/**
		* Stores the value at the top of the stack in the current scope
		* under the name specified by a string in the heap.
		*/
	STORE {
		@Override
		public void execute(VM vm, Instruction instr) {
			int nameIndex = instr.getOperands()[0];

			if (nameIndex < 0 || nameIndex >= vm.getHeap().capacity()) {
				throw new VMException("STORE: name index out of range: " + nameIndex);
			}

			IVMValue nameVal = vm.getHeap().get(nameIndex);
			if (!(nameVal instanceof VMString)) {
				throw new VMException("STORE: operand is not a valid string");
			}
			String name = ((VMString) nameVal).getValue();

			if (vm.getStack().isEmpty()) {
				throw new VMException("STORE: stack underflow when storing variable " + name);
			}

			int valueIndex = vm.getStack().pop();
			vm.getCurrentScope().declareVar(name, valueIndex);
		}
	},

	/**
		* Loads a value from the current scope by name and pushes it onto the stack.
		*/
	LOAD {
		@Override
		public void execute(VM vm, Instruction instr) {
			int nameIndex = instr.getOperands()[0];

			if (nameIndex < 0 || nameIndex >= vm.getHeap().capacity()) {
				throw new VMException("LOAD: name index out of range: " + nameIndex);
			}

			IVMValue nameVal = vm.getHeap().get(nameIndex);
			if (!(nameVal instanceof VMString)) {
				throw new VMException("LOAD: operand is not a valid string");
			}
			String name = ((VMString) nameVal).getValue();

			int valueIndex;
			try {
				valueIndex = vm.getCurrentScope().getVar(name);
			} catch (RuntimeException e) {
				throw new VMException("LOAD: variable not defined: " + name);
			}

			vm.getStack().push(valueIndex);
		}
	},

	/**
		* Enters a new scope by creating a child scope of the current scope.
		*/
	ENTER_SCOPE {
		@Override
		public void execute(VM vm, Instruction instr) {
			vm.setCurrentScope(new VMScope(vm.getCurrentScope()));
		}
	},

	/**
		* Exits the current scope and sets the parent scope as current.
  */
	EXIT_SCOPE {
		@Override
		public void execute(VM vm, Instruction instr) {
			VMScope parent = vm.getCurrentScope().getParent();
			if (parent == null) throw new VMException("Cannot exit the global scope");
			vm.setCurrentScope(parent);
		}
	}
}