package dev.morphe.vm.core.io;

import java.util.Scanner;

/**
 * Implementation of {@link IVMIOHandler} for console input and output.
 * <p>
 * This class handles standard console printing and reading. It provides simple
 * prompts for input and can be used as the default I/O handler for the VM.
 * </p>
 */
public final class ConsoleIOHandler implements IVMIOHandler {

  /**
   * Prints the given string to the console, followed by a newline.
   *
   * @param output The string to print.
   */
  @Override
  public void print(String output) {
    System.out.println(output);
  }

  /**
   * Reads a line of input from the console with a "> " prompt.
   *
   * @return The string entered by the user.
   */
  @Override
  public String read() {
    System.out.print("> ");
    return new Scanner(System.in).nextLine();
  }

  /**
   * Reads a line of input from the console with a "? " prompt.
   *
   * @return The string entered by the user.
   */
  @Override
  public String input() {
    System.out.print("? ");
    return new Scanner(System.in).nextLine();
  }
}