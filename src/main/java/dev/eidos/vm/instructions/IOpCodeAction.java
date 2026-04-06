package dev.eidos.vm.instructions;

import dev.eidos.vm.core.VM;

/**
	* Represents an executable operation in the VM.
	* <p>
	* Implementations of this interface define the behavior of a specific opcode
	* when executed on a {@link VM}. Each {@link Instruction} has an associated
	* IOpCodeAction that performs the operation using its operands.
	* </p>
	*/
public interface IOpCodeAction {

	/**
		* Executes this opcode action on the given VM with the provided instruction.
		*
		* @param vm    The virtual machine instance on which to execute the action.
		* @param instr The instruction containing operands for the action.
		*/
	void execute(VM vm, Instruction instr);
}