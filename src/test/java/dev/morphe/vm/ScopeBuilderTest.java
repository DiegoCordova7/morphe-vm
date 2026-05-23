package dev.morphe.vm;

import dev.morphe.vm.exception.scope.UndefinedVariableException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ScopeBuilderTest extends BaseVMTest {

  @Test
  void shouldStoreAndLoadVariable() {
    pushInt(10);
    builder
        .store("x")
        .load("x");
    assertIntegerResult(10);
  }

  @Test
  void shouldOverwriteVariableInSameScope() {
    pushInt(10);
    builder
        .store("x");
    pushInt(20);
    builder
        .store("x")
        .load("x");
    assertIntegerResult(20);
  }

  @Test
  void shouldAccessOuterVariableInsideNestedScope() {
    pushInt(10);
    builder
        .store("x")
        .enterScope()
        .load("x");

    assertIntegerResult(10);
  }

  @Test
  void shouldShadowOuterVariableInsideNestedScope() {
    pushInt(10);
    builder
        .store("x")
        .enterScope();
    pushInt(20);
    builder
        .store("x")
        .load("x");
    assertIntegerResult(20);
  }

  @Test
  void shouldRestoreOuterVariableAfterLeavingNestedScope() {
    pushInt(10);
    builder
        .store("x")
        .enterScope();
    pushInt(20);
    builder
        .store("x")
        .exitScope()
        .load("x");
    assertIntegerResult(10);
  }

  @Test
  void shouldRemoveLocalVariableAfterLeavingScope() {
    builder
        .enterScope();
    pushInt(99);
    builder
        .store("x")
        .exitScope()
        .load("x");
    assertThrows(UndefinedVariableException.class, this::createAndRunVM);
  }

  @Test
  void shouldThrowWhenLoadingUndefinedVariable() {
    builder
        .load("missing");
    assertThrows(UndefinedVariableException.class, this::createAndRunVM);
  }
}