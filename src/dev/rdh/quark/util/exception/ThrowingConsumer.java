package dev.rdh.quark.util.exception;

/**
 * Represents an operation that accepts a single input argument and returns no result, possibly throwing an exception.
 * @param <T> the type of the input to the operation
 * @see Exceptions#uncheckC(ThrowingConsumer)
 * @see java.util.function.Consumer Consumer
 */
@FunctionalInterface
public interface ThrowingConsumer<T> {
	void accept(T t) throws Throwable;
}
