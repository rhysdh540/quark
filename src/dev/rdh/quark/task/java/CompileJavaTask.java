package dev.rdh.quark.task.java;

import dev.rdh.quark.task.Task;
import dev.rdh.quark.util.FinalizeOnRead;
import dev.rdh.quark.util.JavaUtils;
import dev.rdh.quark.util.ListFinalizeOnRead;
import dev.rdh.quark.util.PathUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

public final class CompileJavaTask extends Task<CompileJavaTask> {

	public final FinalizeOnRead<Path> sourceRoot;
	public final FinalizeOnRead<Path> outputDir;

	public final ListFinalizeOnRead<String> compilerArgs;

	public CompileJavaTask(Path sourceRoot, Path outputDir) {
		this.sourceRoot = FinalizeOnRead.of(sourceRoot);
		this.outputDir = FinalizeOnRead.of(outputDir);
		this.compilerArgs = ListFinalizeOnRead.of();
	}

	@Override
	public String description() {
		return "Compiles Java source files";
	}

	@Override
	protected void doRun() throws Throwable {
		long time = System.currentTimeMillis();
		Path javac = JavaUtils.getJavaBinary("javac");

		if(!Files.exists(javac)) {
			throw new IllegalStateException("javac not found at " + javac);
		}

		Path sourceRoot = this.sourceRoot.get();

		if(!Files.exists(sourceRoot)) {
			throw new IllegalStateException("Source root does not exist: " + sourceRoot);
		}

		if(!Files.isDirectory(sourceRoot)) {
			throw new IllegalStateException("Source root is not a directory: " + sourceRoot);
		}

		if(Files.exists(outputDir.get())) {
			PathUtils.deleteRecursively(outputDir.get());
		}

		Files.createDirectories(outputDir.get());

		Set<Path> allClasses = PathUtils.getChildren(sourceRoot, stream -> stream.filter(p -> !Files.isDirectory(p) && p.toString().endsWith(".java")));

		ProcessBuilder pb = new ProcessBuilder(javac.toString(), "-d", outputDir.get().toString())
			.inheritIO();
		pb.command().addAll(compilerArgs.get());

		for(Path p : allClasses) {
			pb.command().add(p.toString());
		}

		Process p = pb.start();
		p.waitFor();

		if(p.exitValue() != 0) {
			throw new RuntimeException("javac failed with exit code " + p.exitValue());
		}

		long elapsed = System.currentTimeMillis() - time;

		System.out.println("Compiled " + allClasses.size() + " classes in " + elapsed + "ms");
	}
}
