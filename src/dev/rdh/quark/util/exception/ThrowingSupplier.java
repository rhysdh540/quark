package dev.rdh.quark.util.exception;

/**
 * Represents a supplier of results that may throw an exception.
 *
 * @param <T> the type of the result
 * @see Exceptions#uncheckS(ThrowingSupplier)
 * @see java.util.function.Supplier Supplier
 */
@FunctionalInterface
public interface ThrowingSupplier<T> {
	T get() throws Throwable;
}
