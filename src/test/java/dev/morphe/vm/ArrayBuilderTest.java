package dev.morphe.vm;

import dev.morphe.vm.exception.types.array.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ArrayBuilderTest extends BaseVMTest {

  @Test
  void shouldCreateArrayWithCorrectLength() {
    pushInt(3);
    builder
        .newArray()
        .arrayLength();
    assertIntegerResult(3);
  }

  @Test
  void shouldSetAndGetArrayElement() {
    pushInt(3);
    builder
        .newArray()
        .dup();
    pushInt(0);
    pushInt(42);
    builder
        .arraySet();
    pushInt(0);
    builder
        .arrayGet();
    assertIntegerResult(42);
  }

  @Test
  void shouldUpdateArrayElement() {
    pushInt(2);
    builder
        .newArray()
        .dup();
    pushInt(0);
    pushInt(10);
    builder
        .arraySet()
        .dup();
    pushInt(0);
    pushInt(99);
    builder.arraySet();
    pushInt(0);
    builder.arrayGet();
    assertIntegerResult(99);
  }

  @Test
  void shouldReturnErrorWhenArrayIndexIsOutOfBounds() {
    pushInt(1);
    builder
        .newArray();
    pushInt(5);
    builder
        .arrayGet();
    assertThrows(VMArrayIndexOutOfBoundsException.class, this::createAndRunVM);
  }

  @Test
  void shouldReturnErrorWhenArraySizeIsNegative() {
    pushInt(-1);
    builder
        .newArray();
    assertThrows(VMNegativeArraySizeException.class, this::createAndRunVM);
  }

}