package dev.eidos.vm.instructions.opcodes;

import dev.eidos.vm.core.*;
import dev.eidos.vm.core.types.*;
import dev.eidos.vm.instructions.*;

import java.util.function.BiPredicate;

/**
 * Enum representing comparison operations for the VM.
 *
 * <p>
 * Each enum constant implements {@link IOpCodeAction} and defines the behavior
 * of a specific comparison opcode.
 * </p>
 *
 * <p><b>Supported comparison opcodes:</b></p>
 * <ul>
 *   <li>{@link #EQ} - equal to (a == b)</li>
 *   <li>{@link #NEQ} - not equal to (a != b)</li>
 *   <li>{@link #GT} - greater than (a &gt; b)</li>
 *   <li>{@link #LT} - less than (a &lt; b)</li>
 *   <li>{@link #GTE} - greater than or equal to (a &gt;= b)</li>
 *   <li>{@link #LTE} - less than or equal to (a &lt;= b)</li>
 * </ul>
 *
 * <p><b>Supported types for comparison:</b></p>
 * <ul>
 *   <li>{@link VMInteger} and {@link VMDouble} - numeric comparison</li>
 *   <li>{@link VMString} - lexicographical comparison</li>
 *   <li>{@link VMBoolean} - only {@link #EQ} and {@link #NEQ} are valid</li>
 * </ul>
 *
 * <p>
 * Relational operators ({@link #GT}, {@link #LT}, {@link #GTE}, {@link #LTE})
 * are not allowed on booleans. Errors are propagated to the VM stack using
 * {@link VMUtils#pushError(VM, String)}.
 * </p>
 */
public enum ComparisonOpCode implements IOpCodeAction {

  /** Equal (==) */
  EQ((a, b) -> compareValues(a, b) == 0),

  /** Not equal (!=) */
  NEQ((a, b) -> compareValues(a, b) != 0),

  /** Greater than (&gt;) */
  GT((a, b) -> compareValues(a, b) > 0),

  /** Lower than (&lt;) */
  LT((a, b) -> compareValues(a, b) < 0),

  /** Greater or equal than (&lt;=) */
  GTE((a, b) -> compareValues(a, b) >= 0),

  /** Lower or equal than (&lt;=) */
  LTE((a, b) -> compareValues(a, b) <= 0);

  private final BiPredicate<IVMValue, IVMValue> comparator;

  ComparisonOpCode(BiPredicate<IVMValue, IVMValue> comparator) {
    this.comparator = comparator;
  }

  @Override
  public void execute(VM vm, Instruction instr) {
    VMUtils.Operands ops = VMUtils.loadOrPropagate(vm);
    if (ops == null) return;

    IVMValue a = ops.a();
    IVMValue b = ops.b();

    if (isRelationalOp() && a instanceof VMBoolean && b instanceof VMBoolean) {
      VMUtils.pushError(vm, "Invalid boolean comparison for operator " + this);
      return;
    }

    try {
      boolean result = comparator.test(a, b);
      vm.getStack().push(vm.getHeap().alloc(new VMBoolean(result)));

    } catch (IllegalArgumentException e) {
      VMUtils.pushError(vm, e.getMessage());
    }
  }

  private boolean isRelationalOp() {
    return this == GT || this == LT || this == GTE || this == LTE;
  }

  /**
   * Compares two {@link IVMValue} instances and returns an ordering result.
   *
   * <p>
   * This method defines the comparison semantics of the VM.
   * It returns:
   * </p>
   * <ul>
   *   <li><b>0</b> if both values are equal</li>
   *   <li><b>&lt; 0</b> if {@code a} is less than {@code b}</li>
   *   <li><b>&gt; 0</b> if {@code a} is greater than {@code b}</li>
   * </ul>
   *
   * <p><b>Supported comparisons:</b></p>
   * <ul>
   *   <li>
   *     {@link VMInteger} ↔ {@link VMInteger}
   *   </li>
   *   <li>
   *     {@link VMDouble} ↔ {@link VMDouble}
   *   </li>
   *   <li>
   *     Mixed numeric comparison ({@link VMInteger} ↔ {@link VMDouble})
   *     using double precision
   *   </li>
   *   <li>
   *     {@link VMString} ↔ {@link VMString} (lexicographical order)
   *   </li>
   *   <li>
   *     {@link VMBoolean} ↔ {@link VMBoolean}
   *     (equal = 0, different = 1)
   *   </li>
   * </ul>
   *
   * <p>
   * Any other combination of types is considered invalid and results in an exception.
   * </p>
   *
   * @param a the left operand
   * @param b the right operand
   * @return comparison result (&lt; 0, 0, &gt; 0)
   * @throws IllegalArgumentException if values are not comparable
   */
  private static int compareValues(IVMValue a, IVMValue b) {
    if (a instanceof VMInteger ai && b instanceof VMInteger bi) {
      return Integer.compare(ai.getValue(), bi.getValue());
    }
    if (a instanceof VMDouble ad && b instanceof VMDouble bd) {
      return Double.compare(ad.getValue(), bd.getValue());
    }
    if (a instanceof VMInteger ai2 && b instanceof VMDouble bd2) {
      return Double.compare(ai2.getValue(), bd2.getValue());
    }
    if (a instanceof VMDouble ad2 && b instanceof VMInteger bi2) {
      return Double.compare(ad2.getValue(), bi2.getValue());
    }
    if (a instanceof VMString as && b instanceof VMString bs) {
      return as.getValue().compareTo(bs.getValue());
    }
    if (a instanceof VMBoolean ab && b instanceof VMBoolean bb) {
      return ab.get() == bb.get() ? 0 : 1;
    }
    throw new IllegalArgumentException("Cannot compare values of different or unsupported types: "
        + a.getClass().getSimpleName() + " and " + b.getClass().getSimpleName()
    );
  }
}