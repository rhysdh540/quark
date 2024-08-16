package dev.rdh.quark.task;

import dev.rdh.quark.util.FinalizeOnRead;
import dev.rdh.quark.util.exception.ThrowingRunnable;

public class SimpleTask extends Task<SimpleTask> {
	private final ThrowingRunnable action;
	public final FinalizeOnRead<String> description;

	public SimpleTask(ThrowingRunnable action) {
		this.action = action;
		this.description = FinalizeOnRead.of("");
	}

	public SimpleTask() {
		this(() -> {});
	}

	@Override
	public String description() {
		return description.get();
	}

	@Override
	protected void doRun() throws Throwable {
		action.run();
	}
}
