package dev.morphe.vm;

import dev.morphe.vm.core.VM;
import dev.morphe.vm.core.types.VMInteger;
import dev.morphe.vm.exception.stack.StackUnderflowException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public final class StackBuilderTest extends BaseVMTest {

  @Test
  void shouldPushValueOntoStack() {
    pushInt(42);
    assertIntegerResult(42);
  }

  @Test
  void shouldPopTopValueFromStack() {
    pushInt(10);
    pushInt(20);
    builder.pop();
    assertIntegerResult(10);
  }

  @Test
  void shouldDuplicateTopValue() {
    pushInt(5);
    builder.dup();
    VM vm = createAndRunVM();
    int first = vm.getStack().pop();
    int second = vm.getStack().pop();
    assertEquals(heap.get(first), heap.get(second));
    assertEquals(new VMInteger(5), heap.get(first));
  }

  @Test
  void shouldMaintainStackOrder() {
    pushInt(1);
    pushInt(2);
    pushInt(3);
    VM vm = createAndRunVM();
    assertEquals(new VMInteger(3), heap.get(vm.getStack().pop()));
    assertEquals(new VMInteger(2), heap.get(vm.getStack().pop()));
    assertEquals(new VMInteger(1), heap.get(vm.getStack().pop()));
  }

  @Test
  void shouldThrowWhenPopOnEmptyStack() {
    builder.pop();
    assertThrows(StackUnderflowException.class, this::createAndRunVM);
  }

  @Test
  void shouldThrowWhenDupOnEmptyStack() {
    builder.dup();
    assertThrows(StackUnderflowException.class, this::createAndRunVM);
  }
}