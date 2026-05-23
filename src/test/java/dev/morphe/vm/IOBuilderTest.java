package dev.morphe.vm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class IOBuilderTest extends BaseVMTest {

  @Test
  void shouldPrintIntegerValue() {
    pushInt(42);
    builder.print();
    createAndRunVM();
    assertEquals("42", io.output());
  }

  @Test
  void shouldReadStringValue() {
    io.addInput("hello");
    builder.read();
    assertStringResult("hello");
  }

  @Test
  void shouldInputIntegerValue() {
    io.addInput("123");
    builder.input();
    assertIntegerResult(123);
  }

  @Test
  void shouldInputDoubleValue() {
    io.addInput("12.5");
    builder.input();
    assertDoubleResult(12.5);
  }

  @Test
  void shouldInputStringWhenValueIsNotNumeric() {
    io.addInput("morphe");
    builder.input();
    assertStringResult("morphe");
  }
}