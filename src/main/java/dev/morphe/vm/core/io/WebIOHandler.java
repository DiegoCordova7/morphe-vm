package dev.morphe.vm.core.io;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Implementation of {@link IVMIOHandler} for web-based input/output.
 * <p>
 * This class allows VM input and output to be handled asynchronously in a web environment.
 * Outputs are sent to a listener, and inputs are queued for consumption by the VM.
 * </p>
 */
public final class WebIOHandler implements IVMIOHandler {

  private final BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
  private final OutputListener listener;

  /**
   * Listener interface for handling VM output asynchronously.
   */
  public interface OutputListener {
    /**
     * Called whenever the VM prints a string.
     *
     * @param text The text printed by the VM.
     */
    void onOutput(String text);
  }

  /**
   * Constructs a new WebIOHandler with the given output listener.
   *
   * @param listener The listener that will handle VM outputs.
   */
  public WebIOHandler(OutputListener listener) {
    this.listener = listener;
  }

  /**
   * Sends the given string to the output listener.
   *
   * @param s The string to output.
   */
  @Override
  public void print(String s) {
    listener.onOutput(s);
  }

  /**
   * Reads a string from the input queue, blocking until input is available.
   *
   * @return The next input string from the queue, or empty string if interrupted.
   */
  @Override
  public String read() {
    try {
      return inputQueue.take();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return "";
    }
  }

  /**
   * Reads a string from the input queue. Same as {@link #read()}.
   *
   * @return The next input string from the queue.
   */
  @Override
  public String input() {
    return read();
  }

  /**
   * Adds a new input string to the queue.
   *
   * @param input The input string to enqueue.
   */
  public void enqueueInput(String input) {
    inputQueue.add(input);
  }
}