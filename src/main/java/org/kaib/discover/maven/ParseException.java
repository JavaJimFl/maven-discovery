package org.kaib.discover.maven;

/**
 * Indicates an unrecoverable error occurred while parsing a dependency tree.
 */
public class ParseException extends RuntimeException {

	/**
	 * Serial version unique identifier.
	 */
	private static final long serialVersionUID = -6944397489732805685L;

	/**
	 * Constructs a parse exception with the specified detail message. The cause is
	 * not initialized, and may subsequently be initialized by a call to
	 * {@link #initCause}.
	 *
	 * @param message the detail message. The detail message is saved for later
	 *                retrieval by the {@link #getMessage()} method.
	 */
	public ParseException(String message) {
		super(message);
	}

	/**
	 * Constructs a parse runtime exception with the specified detail message and
	 * cause.
	 * <p>
	 * Note that the detail message associated with {@code cause} is <i>not</i>
	 * automatically incorporated in this runtime exception's detail message.
	 *
	 * @param message the detail message (which is saved for later retrieval by the
	 *                {@link #getMessage()} method).
	 * @param cause   the cause (which is saved for later retrieval by the
	 *                {@link #getCause()} method). (A {@code null} value is
	 *                permitted, and indicates that the cause is nonexistent or
	 *                unknown.)
	 */
	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}
}
