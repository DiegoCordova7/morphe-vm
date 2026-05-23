package dev.morphe.vm.instructions.opcodes;

import dev.morphe.vm.core.VM;
import dev.morphe.vm.core.types.IVMValue;
import dev.morphe.vm.core.types.VMBoolean;
import dev.morphe.vm.exception.execution.ExpectedBooleanValueException;
import dev.morphe.vm.instructions.IOpCodeAction;
import dev.morphe.vm.instructions.Instruction;

/**
 * Enum representing control flow operations for the VM.
 * <p>
 * Each enum constant implements {@link IOpCodeAction} and defines
 * the behavior of a specific control opcode, such as halting
 * the VM or performing jumps.
 * </p>
 * <p>
 * Supported control opcodes:
 * </p>
 * <ul>
 *   <li>{@link #HALT} - stops VM execution immediately.</li>
 *   <li>{@link #JMP} - unconditional jump to a specified instruction pointer (IP).</li>
 *   <li>{@link #JMP_IF_FALSE} - jumps if the top of the stack is false (boolean required).</li>
 *   <li>{@link #JMP_IF_TRUE} - jumps if the top of the stack is true (boolean required).</li>
 * </ul>
 */
public enum ControlOpCode implements IOpCodeAction {

  /**
   * Stops the VM execution immediately.
   */
  HALT {
    @Override
    public void execute(VM vm, Instruction instr) {
      vm.stop();
    }
  },

  /**
   * Unconditionally jumps to the specified instruction pointer (IP).
   * The operand is the target IP.
   */
  JMP {
    @Override
    public void execute(VM vm, Instruction instr) {
      int targetIp = instr.getOperands()[0];
      vm.jump(targetIp - 1); // -1 because VM increments IP after each instruction
    }
  },

  /**
   * Jumps to the target IP if the top of the stack is false.
   * Pops a boolean value from the stack and checks it.
   */
  JMP_IF_FALSE {
    @Override
    public void execute(VM vm, Instruction instr) {
      int targetIp = instr.getOperands()[0];
      int condIndex = vm.getStack().pop();
      IVMValue cond = vm.getHeap().get(condIndex);
      if (!(cond instanceof VMBoolean vb)) throw new ExpectedBooleanValueException("JMP_IF_FALSE");
      if (!vb.getValue()) vm.jump(targetIp - 1);
    }
  },

  /**
   * Jumps to the target IP if the top of the stack is true.
   * Pops a boolean value from the stack and checks it.
   */
  JMP_IF_TRUE {
    @Override
    public void execute(VM vm, Instruction instr) {
      int targetIp = instr.getOperands()[0];
      int condIndex = vm.getStack().pop();
      IVMValue cond = vm.getHeap().get(condIndex);
      if (!(cond instanceof VMBoolean vb)) throw new ExpectedBooleanValueException("JMP_IF_TRUE");
      if (vb.getValue()) vm.jump(targetIp - 1);
    }
  }
}