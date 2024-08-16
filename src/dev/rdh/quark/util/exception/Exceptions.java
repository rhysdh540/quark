package dev.rdh.quark.util.exception;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Exceptions {
	private Exceptions() {
	}

	/**
	 * Throws a checked exception as an unchecked exception. This is kind of an ugly hack that gaslights the compiler, but it works.
	 * This is often preferred to wrapping the checked exception in a {@code RuntimeException} because it preserves the original exception type,
	 * and doesn't make the original exception a "cause" of the new exception.
	 * @param t the (possibly) checked exception to throw
	 * @return never returns, throws the exception as an unchecked exception. The return value is there so you can say {@code throw asUnchecked(t);}
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Throwable> RuntimeException asUnchecked(Throwable t) throws T {
		throw (T) t;
	}

	/**
	 * Wraps a {@code ThrowingRunnable} in a {@code Runnable}, so that any exceptions the {@code ThrowingRunnable} throws are rethrown as unchecked exceptions.
	 * @param r the {@code ThrowingRunnable} to wrap
	 * @return a {@code Runnable} that wraps the {@code ThrowingRunnable}
	 * @see ThrowingRunnable
	 * @see Runnable
	 */
	public static Runnable uncheckR(ThrowingRunnable r) {
		return () -> {
			try {
				r.run();
			} catch(Throwable t) {
				throw asUnchecked(t);
			}
		};
	}

	/**
	 * Wraps a {@code ThrowingConsumer} in a {@code Consumer}, so that any exceptions the {@code ThrowingConsumer} throws are rethrown as unchecked exceptions.
	 * @param c the {@code ThrowingConsumer} to wrap
	 * @return a {@code Consumer} that wraps the {@code ThrowingConsumer}
	 * @see ThrowingConsumer
	 * @see Consumer
	 */
	public static <T> Consumer<T> uncheckC(ThrowingConsumer<T> c) {
		return t -> {
			try {
				c.accept(t);
			} catch(Throwable e) {
				throw asUnchecked(e);
			}
		};
	}

	/**
	 * Wraps a {@code ThrowingSupplier} in a {@code Supplier}, so that any exceptions the {@code ThrowingSupplier} throws are rethrown as unchecked exceptions.
	 * @param s the {@code ThrowingSupplier} to wrap
	 * @return a {@code Supplier} that wraps the {@code ThrowingSupplier}
	 * @see ThrowingSupplier
	 * @see Supplier
	 */
	public static <T> Supplier<T> uncheckS(ThrowingSupplier<T> s) {
		return () -> {
			try {
				return s.get();
			} catch(Throwable e) {
				throw asUnchecked(e);
			}
		};
	}

	/**
	 * Wraps a {@code ThrowingFunction} in a {@code Function}, so that any exceptions the {@code ThrowingFunction} throws are rethrown as unchecked exceptions.
	 * @param f the {@code ThrowingFunction} to wrap
	 * @return a {@code Function} that wraps the {@code ThrowingFunction}
	 * @see ThrowingFunction
	 * @see Function
	 */
	public static <I, O> Function<I, O> uncheckF(ThrowingFunction<I, O> f) {
		return i -> {
			try {
				return f.apply(i);
			} catch(Throwable e) {
				throw asUnchecked(e);
			}
		};
	}
}
