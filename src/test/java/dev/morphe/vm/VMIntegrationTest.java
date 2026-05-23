package dev.morphe.vm;

import dev.morphe.vm.core.types.VMInteger;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class VMIntegrationTest extends BaseVMTest {

  @Test
  void shouldExecuteCompleteProgramUsingCoreFeatures() {
    pushInt(0);
    builder
        .store("sum")
        .forLoop(
          () -> {
            pushInt(0);
            builder.store("i");
          },
          () -> {
            builder.load("i");
            pushInt(5);
            builder.lt();
          },
          () -> {
            builder.load("i");
            pushInt(1);
            builder
                .add()
                .store("i");
          },
          () -> {
            builder
                .load("sum")
                .load("i")
                .add()
                .store("sum");
          }
      ).match(
        () -> builder.load("sum"),
        m -> m
            .caseVal(new VMInteger(10), () -> {
              pushString("ok");
              builder.print();
            })
            .caseList(List.of(new VMInteger(5), new VMInteger(15)), () -> {
              pushString("near");
              builder.print();
            })
            .defaultCase(() -> {
              pushString("fail");
              builder.print();
            })
    );
    createAndRunVM();
    assertEquals("ok", io.output());
  }
}