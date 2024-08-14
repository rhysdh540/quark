package dev.rdh.quark.util;

public final class SillyExceptionStuff {
	private SillyExceptionStuff() {
	}

	@SuppressWarnings("unchecked")
	public static <T extends Throwable> RuntimeException asUnchecked(Throwable t) throws T {
		throw (T) t;
	}
}
