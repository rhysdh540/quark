package dev.rdh.quark.util.exception;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Exceptions {
	private Exceptions() {
	}

	@SuppressWarnings("unchecked")
	public static <T extends Throwable> RuntimeException asUnchecked(Throwable t) throws T {
		throw (T) t;
	}

	public static Runnable uncheckR(ThrowingRunnable runnable) {
		return () -> {
			try {
				runnable.run();
			} catch(Throwable t) {
				throw asUnchecked(t);
			}
		};
	}

	public static <T> Consumer<T> uncheckC(ThrowingConsumer<T> cons) {
		return t -> {
			try {
				cons.accept(t);
			} catch(Throwable e) {
				throw asUnchecked(e);
			}
		};
	}

	public static <T> Supplier<T> uncheckS(ThrowingSupplier<T> supp) {
		return () -> {
			try {
				return supp.get();
			} catch(Throwable e) {
				throw asUnchecked(e);
			}
		};
	}

	public static <I, O> Function<I, O> uncheckF(ThrowingFunction<I, O> func) {
		return i -> {
			try {
				return func.apply(i);
			} catch(Throwable e) {
				throw asUnchecked(e);
			}
		};
	}
}
