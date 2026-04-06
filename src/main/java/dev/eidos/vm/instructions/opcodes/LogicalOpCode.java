package dev.eidos.vm.instructions.opcodes;

import dev.eidos.vm.core.*;
import dev.eidos.vm.core.types.*;
import dev.eidos.vm.instructions.*;

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
 * Note: Using operands that are not {@link VMBoolean} will throw an {@link IllegalArgumentException}.
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
          throw new IllegalArgumentException("AND requires boolean operands");
        }
        return new VMBoolean(ab.get() && bb.get());
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
          throw new IllegalArgumentException("OR requires boolean operands");
        }
        return new VMBoolean(ab.get() || bb.get());
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
          throw new IllegalArgumentException("NOT requires boolean operand");
        }
        return new VMBoolean(!b.get());
      }, "NOT failed");
    }
  };
}