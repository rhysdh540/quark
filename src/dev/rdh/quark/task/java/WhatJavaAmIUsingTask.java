package dev.rdh.quark.task.java;

import dev.rdh.quark.task.Task;

public final class WhatJavaAmIUsingTask extends Task<WhatJavaAmIUsingTask> {
	public WhatJavaAmIUsingTask() {
	}

	@Override
	public String description() {
		return "Tells you what version of Java you are using";
	}

	@Override
	protected void doRun() {
		System.out.printf("Running on Java version %s, by %s%n", System.getProperty("java.version"), System.getProperty("java.vendor"));
	}
}
