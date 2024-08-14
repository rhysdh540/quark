package dev.rdh.quark.task.java;

import dev.rdh.quark.task.Task;
import dev.rdh.quark.util.FinalizeOnRead;
import dev.rdh.quark.util.JavaUtils;
import dev.rdh.quark.util.ListFinalizeOnRead;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public final class RunTask extends Task<RunTask> {
	public final FinalizeOnRead<String> mainClass;
	public final ListFinalizeOnRead<String> vmArgs;
	public final ListFinalizeOnRead<String> programArgs;

	public final ListFinalizeOnRead<String> classpath;

	public RunTask() {
		this.mainClass = FinalizeOnRead.mustSet();
		this.vmArgs = ListFinalizeOnRead.of("-Xmx1G");
		this.programArgs = ListFinalizeOnRead.of();
		this.classpath = ListFinalizeOnRead.of("build/classes");
	}

	@Override
	public String description() {
		return "Run a Java application";
	}

	@Override
	protected void doRun() throws Throwable {
		String mainClass = this.mainClass.get();
		List<String> vmArgs = this.vmArgs.get();
		List<String> programArgs = this.programArgs.get();

		Path java = JavaUtils.getJavaBinary("java");

		ProcessBuilder pb = new ProcessBuilder();
		pb.command(java.toString(), "-cp", String.join(File.pathSeparator, this.classpath.get()));
		pb.command().addAll(vmArgs);
		pb.command().add(mainClass);
		pb.command().addAll(programArgs);

		Process p = pb.start();
		p.waitFor();

		if (p.exitValue() != 0) {
			throw new RuntimeException("Process exited with non-zero status code: " + p.exitValue());
		}
	}
}
