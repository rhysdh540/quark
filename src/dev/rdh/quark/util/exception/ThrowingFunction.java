package dev.rdh.quark.util.exception;

/**
 * Represents a function that accepts one argument and produces a result, and may throw a checked exception.
 * @param <I> the type of the input to the function
 * @param <O> the type of the result of the function
 * @see Exceptions#uncheckF(ThrowingFunction)
 * @see java.util.function.Function Function
 */
@FunctionalInterface
public interface ThrowingFunction<I, O> {
	O apply(I input) throws Throwable;
}
