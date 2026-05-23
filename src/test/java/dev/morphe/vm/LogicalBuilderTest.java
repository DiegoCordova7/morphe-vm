package dev.morphe.vm;

import org.junit.jupiter.api.Test;

public final class LogicalBuilderTest extends BaseVMTest {

  @Test
  void shouldPerformLogicalAndTrueTrue() {
    pushBool(true);
    pushBool(true);
    builder.and();
    assertBooleanResult(true);
  }

  @Test
  void shouldPerformLogicalAndTrueFalse() {
    pushBool(true);
    pushBool(false);
    builder.and();
    assertBooleanResult(false);
  }

  @Test
  void shouldPerformLogicalAndFalseFalse() {
    pushBool(false);
    pushBool(false);
    builder.and();
    assertBooleanResult(false);
  }

  @Test
  void shouldPerformLogicalOrTrueFalse() {
    pushBool(true);
    pushBool(false);
    builder.or();
    assertBooleanResult(true);
  }

  @Test
  void shouldPerformLogicalOrFalseFalse() {
    pushBool(false);
    pushBool(false);
    builder.or();
    assertBooleanResult(false);
  }

  @Test
  void shouldPerformLogicalNotTrue() {
    pushBool(true);
    builder.not();
    assertBooleanResult(false);
  }

  @Test
  void shouldPerformLogicalNotFalse() {
    pushBool(false);
    builder.not();
    assertBooleanResult(true);
  }
}