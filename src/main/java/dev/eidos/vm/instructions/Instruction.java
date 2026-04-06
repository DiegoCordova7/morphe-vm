package dev.eidos.vm.instructions;

import java.util.Arrays;

/**
	* Represents a single instruction in the VM.
	* <p>
	* Each instruction consists of an {@link IOpCodeAction} defining the operation
	* to execute, and zero or more integer operands representing heap indices.
	* </p>
	*/
public final class Instruction {

	private final IOpCodeAction action;
	private final int[] operands;

	/**
		* Constructs a new Instruction with the given action and operands.
		*
		* @param action   The operation to execute.
		* @param operands The operands for the instruction (variable number).
		*/
	public Instruction(IOpCodeAction action, int... operands) {
		this.action = action;
		this.operands = operands;
	}

	/**
		* Returns the action of this instruction.
		*
		* @return The IOpCodeAction associated with this instruction.
		*/
	public IOpCodeAction getAction() {
		return action;
	}

	/**
		* Returns the array of operands for this instruction.
		*
		* @return An array of integers representing operands.
		*/
	public int[] getOperands() {
		return operands;
	}

	/**
		* Returns the operand at the specified index.
		*
		* @param i The index of the operand to retrieve.
		* @return The operand value at the given index.
		*/
	public int operand(int i) {
		return operands[i];
	}

	/**
		* Returns a string representation of the instruction.
		*
		* @return A string in the format "action [operands...]".
		*/
	@Override
	public String toString() {
		return action + " " + Arrays.toString(operands);
	}
}