package dev.rdh.quark.util.exception;

/**
 * Represents an operation that accepts no input arguments and returns no result, possibly throwing an exception.
 * @see Exceptions#uncheckR(ThrowingRunnable)
 * @see java.lang.Runnable Runnable
 */
@FunctionalInterface
public interface ThrowingRunnable {
	void run() throws Throwable;
}
