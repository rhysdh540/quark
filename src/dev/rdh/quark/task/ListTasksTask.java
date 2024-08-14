package dev.rdh.quark.task;

import dev.rdh.quark.AbstractBuildscript;

public final class ListTasksTask extends Task<ListTasksTask> {
	private final AbstractBuildscript buildscript;

	public ListTasksTask(AbstractBuildscript buildscript) {
		this.buildscript = buildscript;
	}

	@Override
	public String description() {
		return "List all tasks in the buildscript";
	}

	@Override
	protected void doRun() {
		System.out.println("Tasks:");
		buildscript.tasks.forEach(task -> {
			System.out.println("  -" + task.getName() + ": " + (task.description() == null ? "No description" : task.description()));
			System.out.println("    Type: " + task.getClass().getName());
		});
	}
}
