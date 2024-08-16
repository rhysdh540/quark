package dev.rdh.quark.util;

public final class FinalizeOnRead<T> {
	private T value;
	private boolean finalized;

	private FinalizeOnRead(T value) {
		this.value = value;
		this.finalized = false;
	}

	public static <T> FinalizeOnRead<T> of(T value) {
		return new FinalizeOnRead<>(value);
	}

	@SuppressWarnings("unchecked")
	public static <T> FinalizeOnRead<T> mustSet() {
		return (FinalizeOnRead<T>) new FinalizeOnRead<>(UNSET);
	}

	public T get() {
		if(value == UNSET) {
			throw new IllegalStateException("Value must be set before it can be read");
		}
		finalized = true;
		return value;
	}

	public boolean isFinalized() {
		return finalized;
	}

	public void set(T value) {
		if(finalized) {
			throw new IllegalStateException("Cannot set value after it has been finalized");
		}

		this.value = value;
	}

	@Override
	public String toString() {
		return get().toString();
	}

	private static final Object UNSET = new Object();
}
