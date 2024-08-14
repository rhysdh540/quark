package dev.rdh.quark.task.java;

import dev.rdh.quark.AbstractBuildscript;
import dev.rdh.quark.Main;
import dev.rdh.quark.task.Task;
import dev.rdh.quark.util.FinalizeOnRead;
import dev.rdh.quark.util.JavaUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class JarTask extends Task<JarTask> {
	public final FinalizeOnRead<String> archiveBaseName;
	public final FinalizeOnRead<String> archiveVersion;
	public final FinalizeOnRead<String> archiveClassifier;
	public final FinalizeOnRead<String> jarName;

	public final FinalizeOnRead<Path> destinationDir;

	public final FinalizeOnRead<Path> inputDir;

	public final Map<String, String> manifest;

	public JarTask(AbstractBuildscript b) {
		this.archiveBaseName = FinalizeOnRead.of(b.archiveBaseName.get());
		this.archiveVersion = FinalizeOnRead.of(b.version.get());
		this.archiveClassifier = FinalizeOnRead.of(null);
		this.jarName = FinalizeOnRead.of(null);
		this.destinationDir = FinalizeOnRead.of(b.buildDir.resolve("libs"));
		this.inputDir = FinalizeOnRead.of(b.buildDir.resolve("classes"));
		this.manifest = new LinkedHashMap<>();
		manifest.put("Manifest-Version", "1.0");
	}


	@Override
	public String description() {
		return "Create a JAR file";
	}

	@Override
	protected void doRun() throws Throwable {
		String jarFileName = jarName.get();
		if(jarFileName == null) {
			jarFileName = archiveBaseName.get() + "-" + archiveVersion.get();
			if(archiveClassifier.get() != null) {
				jarFileName += "-" + archiveClassifier.get();
			}
			jarFileName += ".jar";
		}

		Path jarFile = destinationDir.get().resolve(jarFileName);

		Path jarCmd = JavaUtils.getJavaBinary("jar");
		if(!Files.exists(jarCmd)) {
			throw new IllegalStateException("Could not find jar command");
		}

		Path manifestFile = Main.buildscript.buildDir.resolve("jar-manifest.txt");
		Files.write(manifestFile, manifest.entrySet().stream().map(e -> e.getKey() + ": " + e.getValue()).collect(Collectors.toList()));

		ProcessBuilder pb = new ProcessBuilder(jarCmd.toString(), "cfm", jarFile.toString(), manifestFile.toString(),
				"-C", inputDir.get().toString(), ".");
		Process p = pb.start();
		int exitCode = p.waitFor();

		if(exitCode != 0) {
			throw new IllegalStateException("Failed to create JAR file");
		}
	}
}
