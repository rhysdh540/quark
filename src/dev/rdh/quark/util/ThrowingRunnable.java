package dev.rdh.quark.util;

@FunctionalInterface
public interface ThrowingRunnable {
	void run() throws Throwable;
}
