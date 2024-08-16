package dev.rdh.quark.task;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;

public abstract class Task<T extends Task<? super T>> {
	private String name;

	public Task() {
	}

	/**
	 * @return a description of this task.
	 */
	public String description() {
		return null;
	}

	/**
	 * Run this task and all of its dependencies.
	 * @throws Throwable if an error occurs while running the task or any of its dependencies.
	 */
	public final void run() throws Throwable {
		System.out.println("~> Task " + getName());
		this.doRun();
	}

	/**
	 * The actual implementation of the task.
	 * @throws Throwable if an error occurs while running the task.
	 */
	protected abstract void doRun() throws Throwable;

	private final Set<Task<?>> dependencies = new LinkedHashSet<>();

	public final void dependsOn(Task<?>... t) {
		for(Task<?> task : t) {
			if(task == this) {
				throw new RuntimeException("Task cannot depend on itself");
			}

			// TODO make this detect larger circles
			if(task.dependencies.contains(this)) {
				throw new RuntimeException("Circular dependency detected: " + this.getName() + " <-> " + task.getName());
			}

			dependencies.add(task);
		}
	}

	// package-private so it can be accessed by TaskContainer
	final void setName(String name) {
		this.name = name;
	}

	public final String getName() {
		if(name == null) {
			throw new RuntimeException("Task name is null, is it in the task container?");
		}
		return name;
	}

	public final Set<Task<?>> getDependencies() {
		return dependencies;
	}

	@SuppressWarnings("unchecked")
	public final void configure(Consumer<T> c) {
		c.accept((T) this);
	}

	@Override
	public String toString() {
		return String.format("Task '%s'", getName());
	}
}
