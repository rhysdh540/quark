package dev.rdh.quark;

import dev.rdh.quark.task.ListTasksTask;
import dev.rdh.quark.task.SimpleTask;
import dev.rdh.quark.task.TaskContainer;
import dev.rdh.quark.task.java.CompileJavaTask;
import dev.rdh.quark.task.java.JarTask;
import dev.rdh.quark.task.java.WhatJavaAmIUsingTask;
import dev.rdh.quark.util.FinalizeOnRead;
import dev.rdh.quark.util.PathUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public abstract class AbstractBuildscript {
	public final TaskContainer tasks = new TaskContainer();

	protected final Path rootDir = Paths.get("").toAbsolutePath();
	public final Path buildDir = rootDir.resolve("build");

	protected final Properties properties = new Properties();

	public final FinalizeOnRead<String> version = FinalizeOnRead.mustSet();
	public final FinalizeOnRead<String> archiveBaseName = FinalizeOnRead.mustSet();

	public AbstractBuildscript() {
		try {
			Path props = rootDir.resolve(".quark/quark.properties");
			if(Files.exists(props)) {
				properties.load(Files.newBufferedReader(props));
			}
		} catch(Exception e) {
			System.err.println("Failed to load quark.properties: " + e);
		}

		// Remove jvm args/system properties that were set by the wrapper script
		properties.entrySet().removeIf(e -> e.getKey().equals("quark.jvmArgs") || ((String) e.getKey()).startsWith("quark.systemProp."));
	}

	public abstract void configure();

	protected final void setupDefaultTasks() {
		tasks.register("tasks", new ListTasksTask(this));
		tasks.register("build", new SimpleTask()).configure(t -> t.description.set("Build the project"));
		tasks.register("jvm", new WhatJavaAmIUsingTask());

		tasks.register("clean", new SimpleTask(() -> {
			PathUtils.getChildren(buildDir, s -> s.filter(p ->
							p.getParent().equals(buildDir) // gets the direct children of the build directory, but not init
									&& !p.equals(buildDir.resolve("init")))
					).forEach(PathUtils::deleteRecursively);
		})).configure(t -> t.description.set("Delete the build directory"));

		tasks.register("compile", new CompileJavaTask(rootDir.resolve("src"), rootDir.resolve("build/classes"))).configure(t -> {
			tasks.get("build").dependsOn(t);
		});

		tasks.register("jar", new JarTask(this)).configure(t -> {
			tasks.get("build").dependsOn(t);
			t.dependsOn(tasks.get("compile"));
		});
	}

	protected void println(Object o) {
		System.out.println(o);
	}
}
