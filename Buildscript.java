import dev.rdh.quark.AbstractBuildscript;
import dev.rdh.quark.task.java.JarTask;

public class Buildscript extends AbstractBuildscript {
	public void configure() {
		println("quark-ception");
		archiveBaseName.set("quark");
		version.set("0.1.0");

		setupDefaultTasks();

		tasks.get("jar", JarTask.class).configure(t -> {
			t.manifest.put("Main-Class", "dev.rdh.quark.Main");
			t.manifest.put("Implementation-Version", version.get());

			t.jarName.set("quark.jar");
			t.destinationDir.set(rootDir.resolve(".quark"));
		});
	}
}