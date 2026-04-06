package dev.eidos.vm.instructions.opcodes;

import dev.eidos.vm.core.VM;
import dev.eidos.vm.core.VMException;
import dev.eidos.vm.core.VMHeap;
import dev.eidos.vm.core.types.IVMValue;
import dev.eidos.vm.core.types.VMArray;
import dev.eidos.vm.core.types.VMResult;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Utility class providing common operations for the VM.
 * <p>
 * Includes methods for error handling, operand loading,
 * string conversion, and simplified execution of binary
 * and unary operations while respecting VM error propagation.
 * </p>
 */
public final class VMUtils {

  /** Private constructor to prevent instantiation. */
  private VMUtils() {}

  /**
   * Checks if a value is an error result.
   *
   * @param val the value to check
   * @return true if the value is a VMResult with an error
   */
  private static boolean isError(IVMValue val) {
    return val instanceof VMResult r && r.isError();
  }

  /**
   * Allocates an error on the heap and pushes its index onto the stack.
   *
   * @param vm  the virtual machine instance
   * @param msg the error message
   */
  public static void pushError(VM vm, String msg) {
    int errIndex = vm.getHeap().alloc(VMResult.error(msg));
    vm.getStack().push(errIndex);
  }

  /**
   * Pops two operands from the stack and loads their values from the heap.
   * <p>
   * If either operand is an error, it is re-pushed on the stack and null is returned.
   * This ensures error propagation without executing further operations.
   * </p>
   *
   * @param vm the virtual machine instance
   * @return an {@link Operands} record containing the indices and values, or null if an error was propagated
   */
  public static Operands loadOrPropagate(VM vm) {
    int bIndex = vm.getStack().pop();
    int aIndex = vm.getStack().pop();

    IVMValue a = vm.getHeap().get(aIndex);
    IVMValue b = vm.getHeap().get(bIndex);

    if (isError(a)) {
      vm.getStack().push(aIndex);
      return null;
    }

    if (isError(b)) {
      vm.getStack().push(bIndex);
      return null;
    }

    return new Operands(aIndex, bIndex, a, b);
  }

  /**
   * Converts a VM value to its string representation.
   * <p>
   * Supports arrays recursively, using {@link VMHeap} to retrieve array elements.
   * Other types rely on their {@code toString()} method.
   * </p>
   *
   * @param value the value to convert
   * @param heap  the heap containing potential array elements
   * @return the string representation of the value
   */
  public static String valueToString(IVMValue value, VMHeap heap) {
    if (value instanceof VMArray arr) {
      StringBuilder sb = new StringBuilder("[");
      List<Integer> elements = arr.getElements();
      for (int i = 0; i < elements.size(); i++) {
        IVMValue el = heap.get(elements.get(i));
        sb.append(valueToString(el, heap));
        if (i < elements.size() - 1) sb.append(", ");
      }
      sb.append("]");
      return sb.toString();
    }
    return value.toString();
  }

  /**
   * Executes a binary operation on the top two stack operands.
   * <p>
   * Handles error propagation and pushes the result onto the stack.
   * </p>
   *
   * @param vm       the virtual machine instance
   * @param op       the binary operation to execute
   * @param errorMsg the default error message if the operation fails
   */
  public static void binaryOp(VM vm, BiFunction<IVMValue, IVMValue, IVMValue> op, String errorMsg) {
    Operands ops = loadOrPropagate(vm);
    if (ops == null) return;

    try {
      IVMValue result = op.apply(ops.a(), ops.b());
      vm.getStack().push(vm.getHeap().alloc(result));

    } catch (VMException e) {
      pushError(vm, e.getMessage() != null ? e.getMessage() : errorMsg);
    }
  }

  /**
   * Executes a unary operation on the top stack operand.
   * <p>
   * Handles error propagation and pushes the result onto the stack.
   * </p>
   *
   * @param vm       the virtual machine instance
   * @param op       the unary operation to execute
   * @param errorMsg the default error message if the operation fails
   */
  public static void unaryOp(VM vm, Function<IVMValue, IVMValue> op, String errorMsg) {
    int index = vm.getStack().pop();
    IVMValue val = vm.getHeap().get(index);

    if (val instanceof VMResult r && r.isError()) {
      vm.getStack().push(index);
      return;
    }

    try {
      IVMValue result = op.apply(val);
      vm.getStack().push(vm.getHeap().alloc(result));

    } catch (VMException e) {
      pushError(vm, e.getMessage() != null ? e.getMessage() : errorMsg);
    }
  }

  /**
   * Record holding the indices and values of two operands.
   *
   * @param aIndex the heap index of the first operand
   * @param bIndex the heap index of the second operand
   * @param a      the first operand value
   * @param b      the second operand value
   */
  public record Operands(int aIndex, int bIndex, IVMValue a, IVMValue b) {}
}