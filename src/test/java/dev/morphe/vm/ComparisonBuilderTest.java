package dev.morphe.vm;

import org.junit.jupiter.api.Test;

public final class ComparisonBuilderTest extends BaseVMTest {

  @Test
  void shouldCompareEqualIntegers() {
    pushInt(10);
    pushInt(10);
    builder.eq();
    assertBooleanResult(true);
  }

  @Test
  void shouldCompareDifferentIntegers() {
    pushInt(10);
    pushInt(20);
    builder.eq();
    assertBooleanResult(false);
  }

  @Test
  void shouldCompareNotEqualIntegers() {
    pushInt(10);
    pushInt(20);
    builder.neq();
    assertBooleanResult(true);
  }

  @Test
  void shouldCompareGreaterThan() {
    pushInt(20);
    pushInt(10);
    builder.gt();
    assertBooleanResult(true);
  }

  @Test
  void shouldCompareGreaterThanFalse() {
    pushInt(5);
    pushInt(10);
    builder.gt();
    assertBooleanResult(false);
  }

  @Test
  void shouldCompareLessThan() {
    pushInt(5);
    pushInt(10);
    builder.lt();
    assertBooleanResult(true);
  }

  @Test
  void shouldCompareLessThanFalse() {
    pushInt(20);
    pushInt(10);
    builder.lt();
    assertBooleanResult(false);
  }

  @Test
  void shouldCompareGreaterThanOrEqualEqualCase() {
    pushInt(10);
    pushInt(10);
    builder.gte();
    assertBooleanResult(true);
  }

  @Test
  void shouldCompareGreaterThanOrEqualGreaterCase() {
    pushInt(20);
    pushInt(10);
    builder.gte();
    assertBooleanResult(true);
  }

  @Test
  void shouldCompareLessThanOrEqualEqualCase() {
    pushInt(10);
    pushInt(10);
    builder.lte();
    assertBooleanResult(true);
  }

  @Test
  void shouldCompareLessThanOrEqualLessCase() {
    pushInt(5);
    pushInt(10);
    builder.lte();
    assertBooleanResult(true);
  }
}