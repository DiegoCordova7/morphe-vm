package dev.morphe.vm;

import dev.morphe.vm.builder.VMProgramBuilder;
import dev.morphe.vm.core.VM;
import dev.morphe.vm.core.VMHeap;
import dev.morphe.vm.core.VMStack;
import dev.morphe.vm.core.types.*;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public abstract class BaseVMTest {

  protected VMHeap heap;
  protected VMProgramBuilder builder;
  protected TestIOHandler io;

  @BeforeEach
  void setUp() {
    heap = new VMHeap(1024);
    builder = new VMProgramBuilder(heap);
    io = new TestIOHandler();
  }

  protected void pushInt(int value) {
    builder.pushLiteral(new VMInteger(value));
  }

  protected void pushDouble(double value) {
    builder.pushLiteral(new VMDouble(value));
  }

  protected void pushBool(boolean value) {
    builder.pushLiteral(new VMBoolean(value));
  }

  protected void pushString(String value) {
    builder.pushLiteral(new VMString(value));
  }

  protected VM createAndRunVM() {
    VM vm = new VM(builder.build(), new VMStack(1024), heap);
    vm.setIOHandler(io);
    vm.run();
    return vm;
  }

  protected IVMValue topValue(VM vm) {
    int address = vm.getStack().peek();
    return heap.get(address);
  }

  protected IVMValue runAndGetValue() {
    VM vm = createAndRunVM();
    return topValue(vm);
  }

  protected void assertIntegerResult(int expected) {
    assertEquals(new VMInteger(expected), runAndGetValue());
  }

  protected void assertDoubleResult(double expected) {
    assertEquals(new VMDouble(expected), runAndGetValue());
  }

  protected void assertBooleanResult(boolean expected) {
    assertEquals(new VMBoolean(expected), runAndGetValue());
  }

  protected void assertStringResult(String expected) {
    assertEquals(new VMString(expected), runAndGetValue());
  }

  protected void assertErrorResult() {
    IVMValue value = runAndGetValue();
    assertInstanceOf(VMResult.class, value);
    VMResult result = (VMResult) value;
    assertTrue(result.isError());
  }

  protected void assertSuccessResult() {
    IVMValue value = runAndGetValue();
    assertInstanceOf(VMResult.class, value);
    VMResult result = (VMResult) value;
    assertFalse(result.isError());
  }
}