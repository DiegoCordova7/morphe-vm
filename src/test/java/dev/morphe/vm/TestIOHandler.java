package dev.morphe.vm;

import dev.morphe.vm.core.io.IVMIOHandler;

import java.util.ArrayDeque;
import java.util.Queue;

public final class TestIOHandler implements IVMIOHandler {

  private final StringBuilder output = new StringBuilder();
  private final Queue<String> inputs = new ArrayDeque<>();

  public void addInput(String input) {
    inputs.add(input);
  }

  public String output() {
    return output.toString();
  }

  @Override
  public void print(String output) {
    this.output.append(output);
  }

  @Override
  public String read() {
    return inputs.remove();
  }

  @Override
  public String input() {
    return inputs.remove();
  }
}