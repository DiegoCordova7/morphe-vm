package dev.eidos.vm.instructions.opcodes;

import dev.eidos.vm.core.*;
import dev.eidos.vm.core.types.IVMValue;
import dev.eidos.vm.core.types.VMBoolean;
import dev.eidos.vm.instructions.*;

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

      if (!(cond instanceof VMBoolean vb))
        throw new VMException("JMP_IF_FALSE requires a boolean on stack");

      if (!vb.get()) vm.jump(targetIp - 1);
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

      if (!(cond instanceof VMBoolean vb))
        throw new VMException("JMP_IF_TRUE requires a boolean on stack");

      if (vb.get()) vm.jump(targetIp - 1);
    }
  }
}