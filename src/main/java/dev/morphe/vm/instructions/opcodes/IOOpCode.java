package dev.morphe.vm.instructions.opcodes;

import dev.morphe.vm.core.VM;
import dev.morphe.vm.core.types.IVMValue;
import dev.morphe.vm.core.types.VMDouble;
import dev.morphe.vm.core.types.VMInteger;
import dev.morphe.vm.core.types.VMString;
import dev.morphe.vm.instructions.IOpCodeAction;
import dev.morphe.vm.instructions.Instruction;

/**
	* Enum representing I/O operations for the VM.
	* <p>
	* Each enum constant implements {@link IOpCodeAction} and defines the behavior
	* of a specific I/O opcode, including printing values and reading user input.
	* </p>
	* <p>
	* Supported operations:
	* </p>
	* <ul>
	*   <li>{@link #PRINT} - prints the top value on the stack to the VM's output handler.</li>
	*   <li>{@link #READ} - reads a line from the VM's input handler and pushes it as a {@link VMString}.</li>
	*   <li>{@link #INPUT} - reads a line and attempts to parse it as {@link VMInteger} or {@link VMDouble};
	*       defaults to {@link VMString} if parsing fails.</li>
	* </ul>
	*/
public enum IOOpCode implements IOpCodeAction {

	/**
		* Prints the value at the top of the stack using the VM's I/O handler.
		*/
	PRINT {
		@Override
		public void execute(VM vm, Instruction instr) {
			int index = vm.getStack().pop();
			if (index < 0) return;
			IVMValue value = vm.getHeap().get(index);
			String output = VMUtils.valueToString(value, vm.getHeap());
			vm.getIOHandler().print(output);
		}
	},

	/**
		* Reads a line of input from the VM's input handler and pushes it as a VMString.
		*/
	READ {
		@Override
		public void execute(VM vm, Instruction instr) {
			String input = vm.getIOHandler().read();
			VMString str = new VMString(input);
			int idx = vm.getHeap().alloc(str);
			vm.getStack().push(idx);
		}
	},

	/**
		* Reads a line of input and tries to parse it as a VMInteger or VMDouble.
		* If parsing fails, stores it as a VMString. Pushes the result onto the stack.
		*/
	INPUT {
		@Override
		public void execute(VM vm, Instruction instr) {
			String input = vm.getIOHandler().input();
			IVMValue parsed;
			try {
				if (input.contains(".")) {
					parsed = new VMDouble(Double.parseDouble(input));
				} else {
					parsed = new VMInteger(Integer.parseInt(input));
				}
			} catch (NumberFormatException e) {
				parsed = new VMString(input);
			}

			int idx = vm.getHeap().alloc(parsed);
			vm.getStack().push(idx);
		}
	};
}