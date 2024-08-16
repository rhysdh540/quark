package dev.rdh.quark.util.exception;

@FunctionalInterface
public interface ThrowingSupplier<T> {
	T get() throws Throwable;
}
