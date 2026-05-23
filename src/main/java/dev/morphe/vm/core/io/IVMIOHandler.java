package dev.morphe.vm.core.io;

/**
 * Interface for handling input and output operations in the VM.
 * <p>
 * Implementations of this interface allow the VM to perform console or custom I/O.
 * </p>
 */
public interface IVMIOHandler {

  /**
   * Prints the given string to the output.
   *
   * @param output The string to print.
   */
  void print(String output);

  /**
   * Reads a string from the input source without prompting the user.
   *
   * @return The string read.
   */
  String read();

  /**
   * Reads a string from the input source, potentially prompting the user.
   *
   * @return The string read.
   */
  String input();
}