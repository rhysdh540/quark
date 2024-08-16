package dev.rdh.quark.util;

/**
 * Represents a mutable value that can only be changed while it has not been read.
 * After the value has been read, it is considered finalized and cannot be changed.
 * @param <T> The type of the value
 */
public final class FinalizeOnRead<T> {
	private T value;
	private boolean finalized;

	private FinalizeOnRead(T value) {
		this.value = value;
		this.finalized = false;
	}

	/**
	 * Creates a new instance of {@link FinalizeOnRead} with the given value.
	 * @param value The value to store
	 * @return A new instance of {@link FinalizeOnRead}
	 */
	public static <T> FinalizeOnRead<T> of(T value) {
		return new FinalizeOnRead<>(value);
	}

	/**
	 * Creates a new instance of {@link FinalizeOnRead} with an unset value. It is required to set the value before it can be read.
	 * @return A new instance of {@link FinalizeOnRead}
	 */
	@SuppressWarnings("unchecked")
	public static <T> FinalizeOnRead<T> mustSet() {
		return (FinalizeOnRead<T>) new FinalizeOnRead<>(UNSET);
	}

	/**
	 * Gets the value stored in this instance. This should return the same value every time it is called.
	 * @return The value stored in this instance
	 * @throws IllegalStateException If no value has been set
	 */
	public T get() {
		if(value == UNSET) {
			throw new IllegalStateException("Value must be set before it can be read");
		}
		finalized = true;
		return value;
	}

	/**
	 * @return Whether the value has been finalized
	 */
	public boolean isFinalized() {
		return finalized;
	}

	/**
	 * Sets the value stored in this instance to the given value.
	 * @param value The value to store
	 * @return The value previously stored in this instance
	 * @throws IllegalStateException If the value has already been finalized
	 */
	public T set(T value) {
		if(finalized) {
			throw new IllegalStateException("Cannot set value after it has been finalized");
		}

		T oldValue = this.value;
		this.value = value;
		return oldValue;
	}

	/**
	 * @return A String representation of the value stored in this instance
	 * @throws IllegalStateException If no value has been set
	 * @implNote Calling this function will finalize the value;
	 */
	@Override
	public String toString() {
		return get().toString();
	}

	private static final Object UNSET = new Object();
}
