package dev.morphe.vm.instructions.opcodes;

import dev.morphe.vm.core.VM;
import dev.morphe.vm.core.types.VMBoolean;
import dev.morphe.vm.exception.execution.ExpectedBooleanValueException;
import dev.morphe.vm.instructions.IOpCodeAction;
import dev.morphe.vm.instructions.Instruction;

/**
 * Enum representing logical operations for the VM.
 * <p>
 * Each enum constant implements {@link IOpCodeAction} and defines
 * the behavior of a specific logical opcode.
 * All operands must be of type {@link VMBoolean}.
 * </p>
 *
 * <p>
 * Supported logical opcodes:
 * </p>
 * <ul>
 *   <li>{@link #AND} - Logical AND on the top two boolean values of the stack.</li>
 *   <li>{@link #OR}  - Logical OR on the top two boolean values of the stack.</li>
 *   <li>{@link #NOT} - Logical NOT on the top boolean value of the stack.</li>
 * </ul>
 *
 * <p>
 * Note: Using operands that are not {@link VMBoolean} will throw an {@link ExpectedBooleanValueException}.
 * </p>
 */
public enum LogicalOpCode implements IOpCodeAction {

  /**
   * Performs a logical AND on the top two boolean values of the stack.
   */
  AND {
    @Override
    public void execute(VM vm, Instruction instr) {
      VMUtils.binaryOp(vm, (a, b) -> {
        if (!(a instanceof VMBoolean ab) || !(b instanceof VMBoolean bb)) {
          throw new ExpectedBooleanValueException("AND");
        }
        return new VMBoolean(ab.getValue() && bb.getValue());
      }, "AND failed");
    }
  },

  /**
   * Performs a logical OR on the top two boolean values of the stack.
   */
  OR {
    @Override
    public void execute(VM vm, Instruction instr) {
      VMUtils.binaryOp(vm, (a, b) -> {
        if (!(a instanceof VMBoolean ab) || !(b instanceof VMBoolean bb)) {
          throw new ExpectedBooleanValueException("OR");
        }
        return new VMBoolean(ab.getValue() || bb.getValue());
      }, "OR failed");
    }
  },

  /**
   * Performs a logical NOT on the top boolean value of the stack.
   */
  NOT {
    @Override
    public void execute(VM vm, Instruction instr) {
      VMUtils.unaryOp(vm, val -> {
        if (!(val instanceof VMBoolean b)) {
          throw new ExpectedBooleanValueException("NOT");
        }
        return new VMBoolean(!b.getValue());
      }, "NOT failed");
    }
  };
}