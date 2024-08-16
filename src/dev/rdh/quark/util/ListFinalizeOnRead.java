package dev.rdh.quark.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A list that can be modified until it is read, at which point it becomes immutable.
 * @param <T> The type of elements in the list.
 * @see FinalizeOnRead
 */
public final class ListFinalizeOnRead<T> {

	private final ArrayList<T> list;

	private boolean finalized;

	private ListFinalizeOnRead(List<T> list) {
		this.list = new ArrayList<>(list);
		this.finalized = false;
	}

	/**
	 * Create a new instance of ListFinalizeOnRead with the given list.
	 * @param list The list to wrap.
	 * @param <T> The type of elements in the list.
	 * @return A new instance of ListFinalizeOnRead.
	 */
	public static <T> ListFinalizeOnRead<T> of(List<T> list) {
		return new ListFinalizeOnRead<>(list);
	}

	/**
	 * Create a new instance of ListFinalizeOnRead with the given elements.
	 * @param elements The elements to add to the list.
	 * @param <T> The type of elements in the list.
	 * @return A new instance of ListFinalizeOnRead.
	 */
	@SafeVarargs
	public static <T> ListFinalizeOnRead<T> of(T... elements) {
		return new ListFinalizeOnRead<>(Arrays.asList(elements));
	}

	/**
	 * Create a new instance of ListFinalizeOnRead with no elements.
	 * @param <T> The type of elements in the list.
	 * @return A new instance of ListFinalizeOnRead.
	 */
	public static <T> ListFinalizeOnRead<T> empty() {
		return new ListFinalizeOnRead<>(Collections.emptyList());
	}

	private void checkFinalized() {
		if (finalized) {
			throw new IllegalStateException("List is finalized and cannot be modified.");
		}
	}

	private void finalizeList() {
		if (!finalized) {
			finalized = true;
		}
	}

	/**
	 * Get the list. If the list has not been finalized, it will be finalized.
	 * @return The list.
	 */
	public List<T> get() {
		finalizeList();
		return Collections.unmodifiableList(list);
	}

	/**
	 * Set the list to the given elements.
	 * @param elements The elements to set the list to.
	 * @throws IllegalStateException If the list has already been finalized.
	 */
	public void set(Collection<T> elements) {
		checkFinalized();
		list.clear();
		list.addAll(elements);
	}

	/**
	 * Add an element to the list.
	 * @throws IllegalStateException If the list has already been finalized.
	 */
	public void add(T element) {
		checkFinalized();
		list.add(element);
	}

	/**
	 * Add a collection elements to the list.
	 * @throws IllegalStateException If the list has already been finalized.
	 */
	public void addAll(Collection<T> elements) {
		checkFinalized();
		list.addAll(elements);
	}

	/**
	 * Add an array of elements to the list.
	 * @throws IllegalStateException If the list has already been finalized.
	 */
	@SafeVarargs
	public final void addAll(T... elements) {
		checkFinalized();
		list.addAll(Arrays.asList(elements));
	}

	/**
	 * Clear the list.
	 * @throws IllegalStateException If the list has already been finalized.
	 */
	public void clear() {
		checkFinalized();
		list.clear();
	}

	/**
	 * Remove an element from the list.
	 * @throws IllegalStateException If the list has already been finalized.
	 */
	public void remove(T element) {
		checkFinalized();
		list.remove(element);
	}

	/**
	 * Get the size of the list.
	 * @return The size of the list.
	 */
	public int size() {
		return list.size();
	}
}
