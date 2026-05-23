package dev.morphe.vm;

import org.junit.jupiter.api.Test;

public final class ControlFlowBuilderTest extends BaseVMTest {

  @Test
  void shouldExecuteIfBlockWhenConditionIsTrue() {
    builder.ifElse(
        () -> pushBool(true),
        () -> pushInt(10),
        () -> pushInt(20)
    );
    assertIntegerResult(10);
  }

  @Test
  void shouldExecuteElseBlockWhenConditionIsFalse() {
    builder.ifElse(
        () -> pushBool(false),
        () -> pushInt(10),
        () -> pushInt(20)
    );
    assertIntegerResult(20);
  }

  @Test
  void shouldExecuteIfWithoutElseWhenConditionIsTrue() {
    builder.ifElse(
        () -> pushBool(true),
        () -> pushInt(99)
    );
    assertIntegerResult(99);
  }

  @Test
  void shouldExecuteWhileLoop() {
    pushInt(0);
    builder.store("i");
    builder.whileLoop(
        () -> {
          builder.load("i");
          pushInt(3);
          builder.lt();
        },
        () -> {
          builder.load("i");
          pushInt(1);
          builder.add();
          builder.store("i");
        }
    );
    builder.load("i");
    assertIntegerResult(3);
  }

  @Test
  void shouldSkipWhileLoopWhenConditionIsFalse() {
    pushInt(5);
    builder.store("i");
    builder.whileLoop(
        () -> {
          builder.load("i");
          pushInt(3);
          builder.lt();
        },
        () -> {
          builder.load("i");
          pushInt(1);
          builder.add();
          builder.store("i");
        }
    );
    builder.load("i");
    assertIntegerResult(5);
  }

  @Test
  void shouldExecuteDoWhileAtLeastOnce() {
    pushInt(5);
    builder.store("i");
    builder.doWhileLoop(
        () -> {
          builder.load("i");
          pushInt(1);
          builder.add();
          builder.store("i");
        },
        () -> {
          builder.load("i");
          pushInt(3);
          builder.lt();
        }
    );
    builder.load("i");

    assertIntegerResult(6);
  }

  @Test
  void shouldExecuteForLoop() {
    builder.forLoop(
        () -> {
          pushInt(0);
          builder.store("i");
        },
        () -> {
          builder.load("i");
          pushInt(3);
          builder.lt();
        },
        () -> {
          builder.load("i");
          pushInt(1);
          builder.add();
          builder.store("i");
        },
        () -> {}
    );
    builder.load("i");
    assertIntegerResult(3);
  }

  @Test
  void shouldExecuteForLoopBody() {
    pushInt(0);
    builder.store("sum");
    builder.forLoop(
        () -> {
          pushInt(0);
          builder.store("i");
        },
        () -> {
          builder.load("i");
          pushInt(4);
          builder.lt();
        },
        () -> {
          builder.load("i");
          pushInt(1);
          builder.add();
          builder.store("i");
        },
        () -> {
          builder.load("sum");
          builder.load("i");
          builder.add();
          builder.store("sum");
        }
    );
    builder.load("sum");
    assertIntegerResult(6);
  }
}