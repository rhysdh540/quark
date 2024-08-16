package dev.rdh.quark.util.exception;

@FunctionalInterface
public interface ThrowingRunnable {
	void run() throws Throwable;
}
