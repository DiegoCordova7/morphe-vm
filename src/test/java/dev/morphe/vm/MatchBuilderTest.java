package dev.morphe.vm;

import dev.morphe.vm.core.types.VMInteger;
import org.junit.jupiter.api.Test;

import java.util.List;

public final class MatchBuilderTest extends BaseVMTest {

  @Test
  void shouldExecuteMatchingValueCase() {
    builder.match(
        () -> pushInt(2),
        m -> m
            .caseVal(new VMInteger(1), () -> pushInt(10))
            .caseVal(new VMInteger(2), () -> pushInt(20))
            .defaultCase(() -> pushInt(99))
    );
    assertIntegerResult(20);
  }

  @Test
  void shouldExecuteDefaultCaseWhenNoValueMatches() {
    builder.match(
        () -> pushInt(3),
        m -> m
            .caseVal(new VMInteger(1), () -> pushInt(10))
            .caseVal(new VMInteger(2), () -> pushInt(20))
            .defaultCase(() -> pushInt(99))
    );
    assertIntegerResult(99);
  }

  @Test
  void shouldExecuteRangeCaseWhenValueIsInsideRange() {
    builder.match(
        () -> pushInt(4),
        m -> m
            .caseRange(new VMInteger(2), new VMInteger(5), () -> pushInt(50))
            .defaultCase(() -> pushInt(99))
    );
    assertIntegerResult(50);
  }

  @Test
  void shouldExecuteDefaultCaseWhenValueIsOutsideRange() {
    builder.match(
        () -> pushInt(9),
        m -> m
            .caseRange(new VMInteger(2), new VMInteger(5), () -> pushInt(50))
            .defaultCase(() -> pushInt(99))
    );
    assertIntegerResult(99);
  }

  @Test
  void shouldExecuteListCaseWhenAnyValueMatches() {
    builder.match(
        () -> pushInt(3),
        m -> m
            .caseList(
                List.of(new VMInteger(1), new VMInteger(3), new VMInteger(5)),
                () -> pushInt(30)
            )
            .defaultCase(() -> pushInt(99))
    );
    assertIntegerResult(30);
  }

  @Test
  void shouldExecuteOnlyFirstMatchingCase() {
    builder.match(
        () -> pushInt(2),
        m -> m
            .caseVal(new VMInteger(2), () -> pushInt(10))
            .caseRange(new VMInteger(1), new VMInteger(5), () -> pushInt(50))
            .defaultCase(() -> pushInt(99))
    );
    assertIntegerResult(10);
  }
}