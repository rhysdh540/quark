package dev.rdh.quark.task.java;

import dev.rdh.quark.task.Task;
import dev.rdh.quark.util.JavaUtils;

public final class WhatJavaAmIUsingTask extends Task<WhatJavaAmIUsingTask> {
	public WhatJavaAmIUsingTask() {
	}

	@Override
	public String description() {
		return "Tells you what version of Java you are using";
	}

	@Override
	protected void doRun() {
		System.out.printf("Running on Java version %s, by %s%n", JavaUtils.JAVA_VERSION, JavaUtils.JAVA_VENDOR);
		System.out.printf("Java home: %s%n", JavaUtils.JAVA_HOME);
	}
}
