package dev.eidos.vm.instructions.opcodes;

import dev.eidos.vm.core.*;
import dev.eidos.vm.core.types.*;
import dev.eidos.vm.instructions.*;

/**
	* Enum representing arithmetic operations for the VM.
	* <p>
	* Each enum constant implements {@link IOpCodeAction} and defines the behavior
	* of a specific arithmetic opcode.
	* </p>
	*
	* <p>
	* Supported arithmetic opcodes:
	* </p>
	* <ul>
	*   <li>{@link #ADD} - Adds two operands. Supports {@link VMInteger}, {@link VMDouble}, or {@link VMString} concatenation.</li>
	*   <li>{@link #SUB} - Subtracts second operand from first. Supports {@link VMInteger} and {@link VMDouble}.</li>
	*   <li>{@link #MUL} - Multiplies two operands. Supports {@link VMInteger} and {@link VMDouble}.</li>
	*   <li>{@link #DIV} - Divides first operand by second. Supports {@link VMInteger} and {@link VMDouble}. Throws {@link VMException} on division by zero.</li>
	*   <li>{@link #MOD} - Computes modulo. Supports {@link VMInteger} and {@link VMDouble}. Throws {@link VMException} on modulo by zero.</li>
	* </ul>
	*
	* <p>
	* Note: {@link VMString} operands are only valid for ADD (concatenation).
	* Invalid operand types will throw {@link VMException}.
	* </p>
	*/
public enum ArithmeticOpCode implements IOpCodeAction {

	/**
		* Adds two operands. Supports integers, doubles, or string concatenation.
		*/
	ADD {
		@Override
		public void execute(VM vm, Instruction instr) {
			VMUtils.binaryOp(vm, (a, b) -> {
				if (a instanceof VMString || b instanceof VMString)
					return new VMString(a.toString() + b.toString());
				if (a instanceof VMInteger ai && b instanceof VMInteger bi)
					return new VMInteger(ai.getValue() + bi.getValue());
				if (a instanceof VMDouble ad && b instanceof VMDouble bd)
					return new VMDouble(ad.getValue() + bd.getValue());
				throw new VMException("Invalid operands for ADD");
			}, "ADD failed");
		}
	},

	/**
		* Subtracts the second operand from the first. Supports integers and doubles.
		*/
	SUB {
		@Override
		public void execute(VM vm, Instruction instr) {
			VMUtils.binaryOp(vm, (a, b) -> numberOp(a, b, (x, y) -> x - y, "SUB"), "SUB failed");
		}
	},

	/**
		* Multiplies two operands. Supports integers and doubles.
		*/
	MUL {
		@Override
		public void execute(VM vm, Instruction instr) {
			VMUtils.binaryOp(vm, (a, b) -> numberOp(a, b, (x, y) -> x * y, "MUL"), "MUL failed");
		}
	},

	/**
		* Divides the first operand by the second. Supports integers and doubles.
		* Throws ArithmeticException for division by zero.
		*/
	DIV {
		@Override
		public void execute(VM vm, Instruction instr) {
			VMUtils.binaryOp(vm, (a, b) -> {
				double bv = toDouble(b);
				if (Math.abs(bv) < 1e-12)
					throw new VMException("Division by zero");
				return numberOp(a, b, (x, y) -> x / y, "DIV");
			}, "DIV failed");
		}
	},

	/**
		* Computes the modulo of the first operand by the second. Supports integers and doubles.
		* Throws VMException for modulo by zero.
		*/
	MOD {
		@Override
		public void execute(VM vm, Instruction instr) {
			VMUtils.binaryOp(vm, (a, b) -> {
				double bv = toDouble(b);
				if (Math.abs(bv) < 1e-12)
					throw new VMException("Modulo by zero");
				return numberOp(a, b, (x, y) -> x % y, "MOD");
			}, "MOD failed");
		}
	};

	/**
		* Performs a numeric operation (int or double) on two operands.
		*
		* @param a    The first operand.
		* @param b    The second operand.
		* @param op   The operation to apply.
		* @param oper The name of the operation (for exception messages).
		* @return The result as an IVMValue.
		* @throws VMException if operands are not numeric.
		*/
	private static IVMValue numberOp(IVMValue a, IVMValue b, DoubleBinaryOperator op, String oper) {
		if (a instanceof VMInteger ai && b instanceof VMInteger bi)
			return new VMInteger((int) op.apply(ai.getValue(), bi.getValue()));
		if (a instanceof VMDouble ad && b instanceof VMDouble bd)
			return new VMDouble(op.apply(ad.getValue(), bd.getValue()));
		throw new VMException("Invalid operands for " + oper);
	}

	/**
		* Converts a numeric IVMValue to double.
		*
		* @param val The value to convert.
		* @return The double representation.
		* @throws VMException if the value is not numeric.
		*/
	private static double toDouble(IVMValue val) {
		if (val instanceof VMInteger vi) return vi.getValue();
		if (val instanceof VMDouble vd) return vd.getValue();
		throw new VMException("Expected numeric value, got " + val.getClass().getSimpleName());
	}

	/**
		* Functional interface for a binary operation on doubles.
		*/
	@FunctionalInterface
	private interface DoubleBinaryOperator {
		double apply(double a, double b) throws VMException;
	}
}