package dev.eidos.exception;

/**
 * Base exception for all Eidos-related errors.
 * <p>
 * Represents any error produced during the execution
 * of the Eidos toolchain, including lexical analysis,
 * parsing, semantic analysis, compilation, and VM execution.
 * </p>
 */
public abstract class EidosException extends RuntimeException {

  /**
   * Creates a new Eidos exception with the given message.
   *
   * @param message the error description
   */
  protected EidosException(String message) {
    super(message);
  }

  /**
   * Creates a new Eidos exception with the given message and cause.
   *
   * @param message the error description
   * @param cause the original cause
   */
  protected EidosException(String message, Throwable cause) {
    super(message, cause);
  }
}