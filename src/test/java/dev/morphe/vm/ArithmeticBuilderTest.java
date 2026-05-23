package dev.morphe.vm;

import org.junit.jupiter.api.Test;

public final class ArithmeticBuilderTest extends BaseVMTest {

  @Test
  void shouldAddTwoIntegers() {
    pushInt(2);
    pushInt(3);
    builder.add();
    assertIntegerResult(5);
  }

  @Test
  void shouldAddNegativeIntegers() {
    pushInt(-2);
    pushInt(-3);
    builder.add();
    assertIntegerResult(-5);
  }

  @Test
  void shouldAddPositiveAndNegativeIntegers() {
    pushInt(10);
    pushInt(-3);
    builder.add();
    assertIntegerResult(7);
  }

  @Test
  void shouldSubtractTwoIntegers() {
    pushInt(10);
    pushInt(3);
    builder.sub();
    assertIntegerResult(7);
  }

  @Test
  void shouldSubtractResultingNegative() {
    pushInt(3);
    pushInt(10);
    builder.sub();
    assertIntegerResult(-7);
  }

  @Test
  void shouldSubtractNegativeOperand() {
    pushInt(10);
    pushInt(-3);
    builder.sub();
    assertIntegerResult(13);
  }

  @Test
  void shouldMultiplyTwoIntegers() {
    pushInt(4);
    pushInt(5);
    builder.mul();
    assertIntegerResult(20);
  }

  @Test
  void shouldMultiplyPositiveAndNegativeInteger() {
    pushInt(4);
    pushInt(-5);
    builder.mul();
    assertIntegerResult(-20);
  }

  @Test
  void shouldMultiplyByZero() {
    pushInt(999);
    pushInt(0);
    builder.mul();
    assertIntegerResult(0);
  }

  @Test
  void shouldDivideTwoIntegers() {
    pushInt(20);
    pushInt(4);
    builder.div();
    assertIntegerResult(5);
  }

  @Test
  void shouldDivideNegativeIntegers() {
    pushInt(-20);
    pushInt(4);
    builder.div();
    assertIntegerResult(-5);
  }

  @Test
  void shouldDivideResultingZero() {
    pushInt(3);
    pushInt(10);
    builder.div();
    assertIntegerResult(0);
  }

  @Test
  void shouldReturnErrorWhenDividingByZero() {
    pushInt(10);
    pushInt(0);
    builder.div();
    assertErrorResult();
  }

  @Test
  void shouldModuloTwoIntegers() {
    pushInt(20);
    pushInt(6);
    builder.mod();
    assertIntegerResult(2);
  }

  @Test
  void shouldModuloNegativeInteger() {
    pushInt(-20);
    pushInt(6);
    builder.mod();
    assertIntegerResult(-2);
  }

  @Test
  void shouldReturnZeroModuloWhenDivisible() {
    pushInt(20);
    pushInt(5);
    builder.mod();
    assertIntegerResult(0);
  }

  @Test
  void shouldReturnErrorWhenModuloByZero() {
    pushInt(10);
    pushInt(0);
    builder.mod();
    assertErrorResult();
  }
}