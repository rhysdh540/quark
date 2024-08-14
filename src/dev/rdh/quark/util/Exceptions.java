package dev.rdh.quark.util;

public final class Exceptions {
	private Exceptions() {
	}

	@SuppressWarnings("unchecked")
	public static <T extends Throwable> RuntimeException asUnchecked(Throwable t) throws T {
		throw (T) t;
	}

	public static Runnable asUncheckedRunnable(ThrowingRunnable runnable) {
		return () -> {
			try {
				runnable.run();
			} catch(Throwable t) {
				throw asUnchecked(t);
			}
		};
	}
}
