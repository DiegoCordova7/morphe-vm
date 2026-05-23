package dev.morphe.vm.instructions.opcodes;

import dev.morphe.vm.core.VM;
import dev.morphe.vm.instructions.IOpCodeAction;
import dev.morphe.vm.instructions.Instruction;

/**
	* Enum representing stack manipulation operations for the VM.
	* <p>
	* Each enum constant implements {@link IOpCodeAction} and defines
	* a stack operation that interacts with the VM's stack and optionally the heap.
	* </p>
	* <p>
	* Supported operations:
	* </p>
	* <ul>
	*   <li>{@link #PUSH} - pushes a value (by heap index) onto the stack.</li>
	*   <li>{@link #POP} - removes the top value from the stack.</li>
	*   <li>{@link #OVER} - copies the second value on the stack and pushes it on top.</li>
	*   <li>{@link #DUP} - duplicates the top value of the stack.</li>
	*   <li>{@link #LOAD_HEAP} - pushes a specified heap index onto the stack.</li>
	*   <li>{@link #POP_TO_HEAP} - pops the top value from the stack and stores it at a given heap index.</li>
	* </ul>
	*/
public enum StackOpCode implements IOpCodeAction {

	/** Pushes a value (heap index) onto the stack. */
	PUSH {
		@Override
		public void execute(VM vm, Instruction instr) {
			int valueIndex = instr.getOperands()[0];
			vm.getStack().push(valueIndex);
		}
	},

	/** Pops the top value from the stack. */
	POP {
		@Override
		public void execute(VM vm, Instruction instr) {
			vm.getStack().pop();
		}
	},

	/** Pushes a copy of the second value on the stack onto the top. */
	OVER {
		@Override
		public void execute(VM vm, Instruction instr) {
			int topIndex = vm.getStack().pop();
			int secondIndex = vm.getStack().peek();
			vm.getStack().push(topIndex);
			vm.getStack().push(secondIndex);
		}
	},

	/** Duplicates the top value of the stack. */
	DUP {
		@Override
		public void execute(VM vm, Instruction instr) {
			int value = vm.getStack().peek();
			vm.getStack().push(value);
		}
	},

	/** Pushes a given heap index onto the stack. */
	LOAD_HEAP {
		@Override
		public void execute(VM vm, Instruction instr) {
			int index = instr.getOperands()[0];
			vm.getStack().push(index);
		}
	},

	/** Pops the top value from the stack and stores it at the specified heap index. */
	POP_TO_HEAP {
		@Override
		public void execute(VM vm, Instruction instr) {
			int heapIndex = instr.getOperands()[0];
			int stackIndex = vm.getStack().pop();
			vm.getHeap().set(heapIndex, vm.getHeap().get(stackIndex));
		}
	}
}