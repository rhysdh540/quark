package dev.rdh.quark.util.exception;

@FunctionalInterface
public interface ThrowingConsumer<T> {
	void accept(T t) throws Throwable;
}
