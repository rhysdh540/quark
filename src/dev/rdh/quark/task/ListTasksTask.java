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
			String description = task.description();
			if(description == null) {
				description = "No description";
			}
			System.out.println("  -" + task.getName() + ": " + description);
			System.out.println("    Type: " + task.getClass().getName());
			System.out.println();
		});
	}
}
