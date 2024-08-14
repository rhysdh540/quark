package dev.rdh.quark;

import dev.rdh.quark.task.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Main {
	public static AbstractBuildscript buildscript;

	public static void main(String[] args) throws Throwable {
		List<String> argsList = new ArrayList<>(Arrays.asList(args));
		argsList.remove(0); // Remove the buildscript class name from the arguments

		Set<String> tasks = new LinkedHashSet<>();

		for(String arg : argsList) {
			if(arg.equals("-v") || arg.equals("--version")) {
				String version = Main.class.getPackage().getImplementationVersion();
				if(version == null) {
					version = "0.0.0.dev";
				}
				System.out.println("Quark version " + version);
				return;
			}

			if(arg.equals("-h") || arg.equals("--help")) {
				System.out.println("Usage: quark [options] <task>");
				System.out.println("Options:");
				System.out.println("  -v, --version  Print the version of Quark");
				System.out.println("  -h, --help     Print this help message");
				return;
			}

			if(arg.startsWith("-")) {
				throw new RuntimeException("Unknown option: " + arg);
			}

			tasks.add(arg);
		}

		String buildscriptClassName = args[0];
		buildscriptClassName = buildscriptClassName.substring(0, buildscriptClassName.length() - 5); // Remove .java

		Class<?> buildscriptClass = Class.forName(buildscriptClassName, true, Main.class.getClassLoader());
		Object maybeBuildscript = buildscriptClass.getDeclaredConstructor().newInstance();
		if(!(maybeBuildscript instanceof AbstractBuildscript)) {
			throw new RuntimeException("Buildscript class " + buildscriptClass.getName() + " must extend AbstractBuildscript");
		}
		buildscript = (AbstractBuildscript) maybeBuildscript;

		buildscript.configure();

		while(!tasks.isEmpty()) {
			String taskName = tasks.iterator().next();
			tasks.remove(taskName);
			Task<?> task = buildscript.tasks.get(taskName);
			if(task == null) {
				throw new RuntimeException("Task not found: " + taskName);
			}

			for(Task<?> dep : task.getDependencies()) {
				// If the user has already requested this task, don't run it again later
				tasks.remove(dep.getName());
			}

			task.run();
		}
	}
}
