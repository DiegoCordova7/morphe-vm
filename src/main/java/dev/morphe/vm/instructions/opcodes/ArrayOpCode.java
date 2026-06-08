package dev.morphe.vm.instructions.opcodes;

import dev.morphe.vm.core.VM;
import dev.morphe.vm.core.types.IVMValue;
import dev.morphe.vm.core.types.VMArray;
import dev.morphe.vm.core.types.VMInteger;
import dev.morphe.vm.exception.execution.ExpectedArrayValueException;
import dev.morphe.vm.exception.execution.ExpectedIntegerValueException;
import dev.morphe.vm.exception.types.array.VMNegativeArraySizeException;
import dev.morphe.vm.instructions.IOpCodeAction;
import dev.morphe.vm.instructions.Instruction;

/**
 * Enum representing array operations for the VM.
 * <p>
 * Each enum constant implements {@link IOpCodeAction} and defines the behavior
 * of a specific array opcode, including creation, access, assignment, and length retrieval.
 * </p>
 * <p>
 * Supported array opcodes:
 * </p>
 * <ul>
 *   <li>{@link #ARRAY_NEW} - creates a new array of a given size (from stack) and pushes its heap index.</li>
 *   <li>{@link #ARRAY_GET} - retrieves an element from an array at a specified index.</li>
 *   <li>{@link #ARRAY_SET} - sets a value at a specified index in an array.</li>
 *   <li>{@link #ARRAY_LENGTH} - retrieves the length of the array.</li>
 * </ul>
 */
public enum ArrayOpCode implements IOpCodeAction {

  /**
   * Creates a new array with a size provided on the stack.
   * Pushes the heap index of the new array onto the stack.
   */
  ARRAY_NEW {
    @Override
    public void execute(VM vm, Instruction instr) {
      int sizeIndex = vm.getStack().pop();
      IVMValue sizeVal = vm.getHeap().get(sizeIndex);
      if (!(sizeVal instanceof VMInteger si)) throw new ExpectedIntegerValueException("ARRAY_NEW");
      int size = si.getValue();
      if (size < 0) throw new VMNegativeArraySizeException(size);
      VMArray array = new VMArray(size);
      int arrayIndex = vm.getHeap().alloc(array);
      vm.getStack().push(arrayIndex);
    }
  },

  ARRAY_NEW_MULTI {
    @Override
    public void execute(VM vm, Instruction instr) {
      int dimensions = instr.operand(0);

      int[] sizes = new int[dimensions];

      for (int i = dimensions - 1; i >= 0; i--) {
        int sizeIndex = vm.getStack().pop();
        IVMValue sizeVal = vm.getHeap().get(sizeIndex);

        if (!(sizeVal instanceof VMInteger si)) {
          throw new ExpectedIntegerValueException("ARRAY_NEW_MULTI");
        }

        int size = si.getValue();

        if (size < 0) {
          throw new VMNegativeArraySizeException(size);
        }

        sizes[i] = size;
      }

      int arrayIndex = createMultiArray(vm, sizes, 0);
      vm.getStack().push(arrayIndex);
    }

    private int createMultiArray(VM vm, int[] sizes, int depth) {
      VMArray array = new VMArray(sizes[depth]);
      int arrayIndex = vm.getHeap().alloc(array);

      if (depth < sizes.length - 1) {
        for (int i = 0; i < sizes[depth]; i++) {
          int childIndex = createMultiArray(vm, sizes, depth + 1);
          array.set(i, childIndex);
        }
      }

      return arrayIndex;
    }
  },

  /**
   * Retrieves the element at the given index from the array.
   * Pops array index and element index from the stack, pushes the value index.
   */
  ARRAY_GET {
    @Override
    public void execute(VM vm, Instruction instr) {
      int indexIndex = vm.getStack().pop();
      int arrayIndex = vm.getStack().pop();
      IVMValue arrVal = vm.getHeap().get(arrayIndex);
      IVMValue idxVal = vm.getHeap().get(indexIndex);
      if (!(arrVal instanceof VMArray arr)) throw new ExpectedArrayValueException("ARRAY_GET");
      if (!(idxVal instanceof VMInteger ii)) throw new ExpectedIntegerValueException("ARRAY_GET index");
      int valueIndex = arr.get(ii.getValue());
      vm.getStack().push(valueIndex);
    }
  },

  /**
   * Sets the value at a given index in the array.
   * Pops value index, element index, and array index from the stack.
   */
  ARRAY_SET {
    @Override
    public void execute(VM vm, Instruction instr) {
      int valueIndex = vm.getStack().pop();
      int indexIndex = vm.getStack().pop();
      int arrayIndex = vm.getStack().pop();
      IVMValue arrVal = vm.getHeap().get(arrayIndex);
      IVMValue idxVal = vm.getHeap().get(indexIndex);
      if (!(arrVal instanceof VMArray arr)) throw new ExpectedArrayValueException("ARRAY_SET");
      if (!(idxVal instanceof VMInteger ii)) throw new ExpectedIntegerValueException("ARRAY_SET index");
      arr.set(ii.getValue(), valueIndex);
    }
  },

  /**
   * Returns the length of the array.
   * Pops array index from the stack and pushes the length as a VMInteger.
   */
  ARRAY_LENGTH {
    @Override
    public void execute(VM vm, Instruction instr) {
      int arrayIndex = vm.getStack().pop();
      IVMValue arrVal = vm.getHeap().get(arrayIndex);
      if (!(arrVal instanceof VMArray arr)) throw new ExpectedArrayValueException("ARRAY_LENGTH");
      int lenIndex = vm.getHeap().alloc(new VMInteger(arr.length()));
      vm.getStack().push(lenIndex);
    }
  }
}