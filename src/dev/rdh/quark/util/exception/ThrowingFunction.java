package dev.rdh.quark.util.exception;

public interface ThrowingFunction<I, O> {
	O apply(I input) throws Throwable;
}
