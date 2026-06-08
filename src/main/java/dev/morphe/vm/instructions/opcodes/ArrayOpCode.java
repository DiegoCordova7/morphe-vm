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
 * Enum representing array-related VM opcodes.
 * <p>
 * Arrays in the VM are heap-allocated values represented by {@link VMArray}.
 * A {@code VMArray} stores heap indices, not raw values. Therefore, every
 * array element is a reference to another heap slot.
 * </p>
 *
 * <p>
 * This design also allows multidimensional arrays to be represented as
 * arrays of arrays. For example, {@code Integer[][]} is stored as a
 * {@code VMArray} whose elements point to other {@code VMArray} instances.
 * </p>
 *
 * <ul>
 *   <li>{@link #ARRAY_NEW} - creates a one-dimensional array.</li>
 *   <li>{@link #ARRAY_NEW_MULTI} - creates a multidimensional array.</li>
 *   <li>{@link #ARRAY_GET} - retrieves an array element reference.</li>
 *   <li>{@link #ARRAY_SET} - stores a heap reference into an array element.</li>
 *   <li>{@link #ARRAY_LENGTH} - pushes the length of an array.</li>
 * </ul>
 */
public enum ArrayOpCode implements IOpCodeAction {

  /**
   * Creates a one-dimensional array.
   * <p>
   * Stack before execution:
   * </p>
   * <pre>
   * [..., sizeIndex]
   * </pre>
   *
   * <p>
   * {@code sizeIndex} must point to a {@link VMInteger} in the heap.
   * The opcode allocates a {@link VMArray} of that size and pushes the
   * heap index of the created array.
   * </p>
   *
   * <p>
   * Stack after execution:
   * </p>
   * <pre>
   * [..., arrayIndex]
   * </pre>
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

  /**
   * Creates a multidimensional array.
   * <p>
   * The number of dimensions is provided as the first instruction operand:
   * </p>
   * <pre>
   * ARRAY_NEW_MULTI [dimensions]
   * </pre>
   *
   * <p>
   * Stack before execution for {@code dimensions = n}:
   * </p>
   * <pre>
   * [..., size1Index, size2Index, ..., sizeNIndex]
   * </pre>
   *
   * <p>
   * Each size index must point to a {@link VMInteger}. The opcode consumes
   * all dimension sizes, creates nested {@link VMArray} instances recursively,
   * and pushes the heap index of the outermost array.
   * </p>
   *
   * <p>
   * Stack after execution:
   * </p>
   * <pre>
   * [..., outerArrayIndex]
   * </pre>
   *
   */
  ARRAY_NEW_MULTI {
    @Override
    public void execute(VM vm, Instruction instr) {
      int dimensions = instr.operand(0);
      int[] sizes = new int[dimensions];
      for (int i = dimensions - 1; i >= 0; i--) {
        int sizeIndex = vm.getStack().pop();
        IVMValue sizeVal = vm.getHeap().get(sizeIndex);
        if (!(sizeVal instanceof VMInteger si)) throw new ExpectedIntegerValueException("ARRAY_NEW_MULTI");
        int size = si.getValue();
        if (size < 0) throw new VMNegativeArraySizeException(size);
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