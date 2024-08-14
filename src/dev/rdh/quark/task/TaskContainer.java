package dev.rdh.quark.task;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskContainer implements Iterable<Task<?>> {
	private final Map<String, Task<?>> tasks;

	public TaskContainer() {
		this.tasks = new LinkedHashMap<>();
	}

	public Task<?> get(String name) {
		if(name == null) {
			throw new IllegalArgumentException("Task name cannot be null");
		}

		Task<?> task = tasks.get(name);
		if(task == null) {
			throw new IllegalArgumentException("Task " + name + " not found");
		}
		return task;
	}

	public <T extends Task<? super T>> List<T> getByType(Class<T> type) {
		return tasks.values().stream().filter(type::isInstance).map(type::cast).collect(Collectors.toList());
	}

	public <T extends Task<? super T>> T get(String name, Class<T> type) {
		if(name == null) {
			throw new IllegalArgumentException("Task name cannot be null");
		}

		Task<?> task = tasks.get(name);
		if(task == null) {
			throw new IllegalArgumentException("Task " + name + " not found");
		}
		if(!type.isInstance(task)) {
			throw new IllegalArgumentException("Task " + name + " is not of type " + type.getName() + " (is " + task.getClass().getName() + ")");
		}
		return type.cast(task);
	}

	public <T extends Task<? super T>> T register(String name, T task) {
		if(name == null) {
			throw new IllegalArgumentException("Task name cannot be null");
		}

		if(tasks.containsKey(name)) {
			throw new IllegalArgumentException("Task " + name + " already exists");
		}
		tasks.put(name, task);
		task.setName(name);
		return task;
	}

	public Task<?> remove(String name) {
		if(name == null) {
			throw new IllegalArgumentException("Task name cannot be null");
		}

		Task<?> t = tasks.remove(name);
		if(t == null) {
			throw new IllegalArgumentException("Task " + name + " not found");
		}
		t.setName(null);
		return t;
	}

	public boolean contains(String name) {
		if(name == null) {
			throw new IllegalArgumentException("Task name cannot be null");
		}

		return tasks.containsKey(name);
	}

	public Iterator<Task<?>> iterator() {
		return tasks.values().iterator();
	}
}
