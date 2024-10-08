package dev.rdh.quark;

import dev.rdh.quark.task.Task;
import dev.rdh.quark.util.exception.Exceptions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Main {
	public static AbstractBuildscript buildscript;

	public static void main(String[] args) {
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
		if(!buildscriptClassName.endsWith(".java")) {
			throw new RuntimeException("somehow you compiled a non-java file");
		}

		buildscriptClassName = buildscriptClassName.substring(0, buildscriptClassName.length() - 5).replace('/', '.');

		try {
			Class<?> buildscriptClass = Class.forName(buildscriptClassName, true, Main.class.getClassLoader());
			Constructor<?> constructor = buildscriptClass.getConstructor();
			constructor.setAccessible(true);
			Object maybeBuildscript = constructor.newInstance();

			if(!(maybeBuildscript instanceof AbstractBuildscript)) {
				throw new RuntimeException("Buildscript class " + buildscriptClass.getName() + " must extend AbstractBuildscript");
			}

			buildscript = (AbstractBuildscript) maybeBuildscript;

		} catch(ClassNotFoundException e) { // class doesn't exist
			throw new RuntimeException("Buildscript class " + buildscriptClassName + " not found");
		} catch (NoSuchMethodException e) { // no no-arg constructor
			throw new RuntimeException("Buildscript class " + buildscriptClassName + " must have a no-arg constructor");
		} catch (InvocationTargetException e) { // constructor threw an exception
			throw Exceptions.asUnchecked(e);
		} catch (InstantiationException e) { // abstract class
			throw new RuntimeException("Buildscript class " + buildscriptClassName + " must be concrete");
		} catch (IllegalAccessException e) { // should never happen, we setAccessible(true)
			throw new RuntimeException("what?", e);
		}

		System.out.println("~> Configuring project " + buildscript.rootDir);
		buildscript.configure();

		Set<Task<?>> tasksToRun = new LinkedHashSet<>();
		for(String taskName : tasks) {
			Task<?> task = buildscript.tasks.get(taskName);
			if(task == null) {
				throw new RuntimeException("Task " + taskName + " not found");
			}

			tasksToRun.addAll(task.getDependencies());
			tasksToRun.add(task);
		}

		tasksToRun.forEach(Exceptions.uncheckC(Task::run));
	}
}
