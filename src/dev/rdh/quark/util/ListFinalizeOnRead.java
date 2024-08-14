package dev.rdh.quark.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public final class ListFinalizeOnRead<T> {
	private final List<T> list;

	private boolean finalized;

	private ListFinalizeOnRead(List<T> list) {
		this.list = list;
		this.finalized = false;
	}

	public static <T> ListFinalizeOnRead<T> of(List<T> list) {
		return new ListFinalizeOnRead<>(list);
	}

	@SafeVarargs
	public static <T> ListFinalizeOnRead<T> of(T... elements) {
		return new ListFinalizeOnRead<>(new ArrayList<>(Arrays.asList(elements)));
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

	public List<T> get() {
		finalizeList();
		return list;
	}

	public void set(Collection<T> elements) {
		checkFinalized();
		list.clear();
		list.addAll(elements);
	}

	public void add(T element) {
		checkFinalized();
		list.add(element);
	}

	public void addAll(Collection<T> elements) {
		checkFinalized();
		list.addAll(elements);
	}

	@SafeVarargs
	public final void addAll(T... elements) {
		checkFinalized();
		list.addAll(Arrays.asList(elements));
	}

	public void clear() {
		checkFinalized();
		list.clear();
	}

	public void remove(T element) {
		checkFinalized();
		list.remove(element);
	}
}
